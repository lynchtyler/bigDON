package com.flashboomlet.scavenger.articles.nyt

import java.net.URL

import com.fasterxml.jackson.databind.ObjectMapper
import com.flashboomlet.scavenger.errors.SearchError
import com.flashboomlet.data.PrefetchArticle
import com.flashboomlet.scavenger.Scavenger
import com.flashboomlet.data.models.Entity
import com.flashboomlet.data.models.NewYorkTimesArticle
import com.flashboomlet.db.MongoDatabaseDriver
import com.flashboomlet.data.models.MetaData
import com.flashboomlet.data.models.PreprocessData
import com.flashboomlet.preproccessing.FastSentimentClassifier
import com.flashboomlet.preproccessing.CountUtil.countContent
import com.flashboomlet.preproccessing.DateUtil
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try
import scalaj.http.Http
import scalaj.http.HttpRequest
import scalaz.-\/
import scalaz.\/
import scalaz.\/-

/**
  * Class that fetches articles and other trending topics from the New York Times website
  *
  * @param apiKeys Wrapper for list of Api keys. Each endpoint needs its own apikey
  */
class NewYorkTimesScavenger(apiKeys: NewYorkTimesApiKeys)(implicit val mapper: ObjectMapper,
  implicit val db: MongoDatabaseDriver) extends Scavenger with LazyLogging {

  private[this] val BaseApiPath: String = "https://api.nytimes.com/svc/search/v2"

  private[this] val ItemsPerPage: Int = 10

  private[this] val OKResponseString: String = "OK"

  private[this] val PolitenessDelay: Int = 5000

  private[this] val TimeoutAllowed = 60000


  /**
    * Scaffold for the scavengerTrait
    */
  def scavenge(entities: Set[Entity]): Unit = {
   // entities foreach search terms foreach scavengeArticles(searchterms, today, today)
   val yesterdatStringQuery = DateUtil.getNytYesterday
   val todayStringQuery = DateUtil.getNytToday
   entities.foreach { entity =>
     entity.searchTerms.foreach { term =>
       Try {
         Thread.sleep(PolitenessDelay)
         scavengeArticles(
           query = term,
           entityLastName = entity.lastName,
           beginDate = yesterdatStringQuery,
           endDate = todayStringQuery)
         logger.info(s"Successfully scavenged articles for ${entity.lastName} : $term")
       }.getOrElse(
         logger.error("Failed to scavenge Articles")
       )
     }
   }
  }

  /**
    * Given the query parameters needed, this will fetch articles, analyze them, and store them
    * from the New York Times.
    *
    * @param query The query string to search
    * @param beginDate The starting date of articles to retrieve (YYYYMMDD). Defaults to beginning
    *                  of time.
    * @param endDate The ending date of articles to retrieve (YYYYMMDD). Defaults to beginning of
    *                time
    */
  private[this] def scavengeArticles(
    query: String,
    entityLastName: String,
    beginDate: String = "",
    endDate: String = ""): Unit = {

    val parameters = Seq(
      ("q", query),
      ("begin_date", beginDate),
      ("end_date", endDate))

    fetchArticleSearchQueryMetaDatas(parameters) match {
      case \/-(numPages) =>
        prefetchArticles(query = query, entityLastName = entityLastName, parameters, numPages).map {
        (articles: Seq[PrefetchArticle]) =>
          articles.foreach { (article: PrefetchArticle) =>
            // Fetch article
            constructModelAndInsert(article, query = query, entityLastName =  entityLastName)
          }
        }
      case -\/(err) => logger.error(err.getMessage)
    }
  }

  private[this] def constructModelAndInsert(
      article: PrefetchArticle,
      query: String,
      entityLastName: String): Unit = {
    Try {
      val htmlDoc: Document = Jsoup.parse(new URL(article.url), TimeoutAllowed)
      val articleBody: String = htmlDoc.getElementsByClass("story-body-text").text()
      Try {
        val metaData: MetaData = MetaData(
          fetchDate = article.fetchDate,
          publishDate = article.publishDate,
          source = article.source,
          searchTerm = query,
          entityLastName = entityLastName,
          contributions = article.keyPeople.size)

        if (db.newYorkTimesArticleExists(article.url)) {
          db.addNytMetaData(article.url, metaData)
          logger.info(s"Updating metadata for article ${article.url}")
        } else {
          val preprocessData: PreprocessData = PreprocessData(
            sentiment = FastSentimentClassifier.getSentiment(
              articleBody
            ),
            counts = countContent(article.title, articleBody, query))

          val nytArticle = NewYorkTimesArticle(
            url = article.url,
            title = article.title,
            author = article.author,
            body = articleBody,
            summaries = article.summaries,
            keyPeople = article.keyPeople,
            metaDatas = Set(metaData),
            preprocessData = preprocessData
          )
          db.insertNYTArticle(nytArticle)
          logger.info(s"Successfully inserted article ${article.url}")
        }
      }.getOrElse(logger.error(s"Failed to create and insert NYT article: $query : ${article.url}"))
    }.getOrElse(logger.error(s"Failed to Parse article $query + ${article.url}"))
  }

  /**
    * Searches New York Time's articlesearch.json api
    *
    * Slides through pagination and returns a lazy sequence of prefetched articles.
    *
    * @param queryParameters A sequence of query key and value pairs used as URI request parameters
    * @param numPages The number of pages of results that exist given the query parameters
    * @return A future sequence of all of the prefetched articles
    */
  private[this] def prefetchArticles(
      query: String,
      entityLastName: String,
      queryParameters: Seq[(String, String)],
      numPages: Int): Future[Seq[PrefetchArticle]] = {

    val parameters: Seq[(String, String)] =  ("api-key", apiKeys.articleSearch) +: queryParameters

    val request: HttpRequest = Http(BaseApiPath + "/articlesearch.json").params(parameters)

    Future {
      (0 until numPages).asInstanceOf[Seq[Int]].flatMap { (pageNum: Int) =>
        Try { global.wait(PolitenessDelay) }.getOrElse(()) // logging would also help here bro
        getPrefetchArticlesFromPage(query, entityLastName, request, pageNum)
      }
    }
  }

  /**
    * Tests a query out to determine if it is valid and then either returns an error or retrieves
    * the metadata associated with the query.
    *
    * @param queryParameters The sequence of query parameter key value pairs
    * @return Error on the left if one exists, else, the number of pages of results for the valid
    *         query on the right.
    */
  private[this] def fetchArticleSearchQueryMetaDatas(
      queryParameters: Seq[(String, String)]): \/[Error, Int] = {

    val parameters: Seq[(String, String)] = ("api-key", apiKeys.articleSearch) +: queryParameters

    val request: HttpRequest = Http(BaseApiPath + "/articlesearch.json").params(parameters)

    val metaDatas = mapper.readValue(
      request.param("fl", "_id").asBytes.body, classOf[SearchMetaData])

    if (metaDatas.status == OKResponseString) { // we have a valid search
      val numHits = metaDatas.response.meta.hits
      val totalPages = (numHits / ItemsPerPage) + { if (numHits % ItemsPerPage == 0) 0 else 1 }
      \/-(totalPages)
    } else {
      -\/(new SearchError(
        s"The requested search parameters returned an invalid status: ${metaDatas.status}"))
    }
  }

  /**
    * Safely constructs a prefetch article based on the potentially present information from the
    * response JSON for a New York Time's article.
    *
    * @param request The request to retrieve the page of article JSON responses
    * @param pageNum The page of results to retrieve
    * @return A sequence of all prefetch articles from the page that was fetched
    */
  private[this] def getPrefetchArticlesFromPage(
      query: String,
      entityLastName: String,
      request: HttpRequest,
      pageNum: Int): Seq[PrefetchArticle] = {

    mapper.readValue(request.param("page", s"$pageNum").asBytes.body, classOf[ArticleResponse])
      .response.docs.toSeq
      .filter { (d: Doc) =>
        (d.document_type.contains("blogpost") || d.document_type.contains("article")) &&
        !db.isNytDuplicate(d.web_url, query, entityLastName) } // remove duplicates
      .map { (doc: Doc) =>
        PrefetchArticle(
          url = doc.web_url,
          title = getString(doc.headline.main),
          subjects = Set(getString(doc.section_name), getString(doc.subsection_name)),
          summaries = Set(getString(doc.snippet), getString(doc.lead_paragraph)),
          publishDate = DateUtil.getNytInMillis(getString(doc.pub_date)),
          fetchDate = DateUtil.getNowInMillis,
          source = getString(doc.source),
          author = getAuthorFromByLine(doc.byline),
          keyPeople = Set(getContributorsFromByLine(doc.byline)),
          keyWords = getKeyWords(doc.keywords),
          wordCount = Option(doc.word_count)
        )
    }
  }

  private[this] def getKeyWords(keyword: Set[Keyword]): Set[String] = {
    Option(keyword) match {
      case Some(ks) => ks.map((k: Keyword) => getString(k.value))
      case None => Set()
    }
  }

  private[this] def getContributorsFromByLine(byLine: ByLine): String = {
    Option(byLine) match {
      case Some(b) => getString(b.contributor)
      case None => ""
    }
  }

  private[this] def getAuthorFromByLine(byline: ByLine): String = {
    Option(byline) match {
      case Some(b) => b.person.headOption match {
        case Some(p) =>
          getString(p.firstname) + " " +
          getString(p.middlename) + " " +
          getString(p.lastname)
        case None => ""
      }
      case None => ""
    }
  }

  private[this] def getString(field: String): String = Option(field).getOrElse("")
}

/** Companion object with a constructor that retrieves configurations */
object NewYorkTimesScavenger {

  private[this] val ConfigurationFileName = "newyorktimes.conf"

  private[this] val ArticleSearchApiKeyString = "articlesearch_apikey"

  private[this] val MostPopularApiKeyString = "mostpopular_apikey"

  /**
    * Constructor for the NewYorkTimeScavenger.
    *
    * This method extracts all relevant information from the corresponding
    * com.flashboomlet.scavenger.twitter.configuration file.
    *
    * @note There must be a valid `newyorktimes.conf` file in the resources directory.
    * @return A new instance of a NewYorkTimesScavenger.
    */
  def apply()(implicit mapper: ObjectMapper, db: MongoDatabaseDriver): NewYorkTimesScavenger = {

    val config: Config = ConfigFactory.parseResources(ConfigurationFileName)
    new NewYorkTimesScavenger(
      NewYorkTimesApiKeys(
        articleSearch = config.getString(ArticleSearchApiKeyString),
        mostPopular = config.getString(MostPopularApiKeyString)
      )
    )
  }
}

