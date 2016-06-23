package com.flashboomlet.db.implicits

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.MetaData
import com.flashboomlet.data.models.NewYorkTimesArticle
import com.flashboomlet.data.models.PreprocessData
import com.flashboomlet.db.MongoUtil
import reactivemongo.bson.BSONArray
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONString

/** Implicit readers and writers for the NYT model in the NYT articles collection of MongoDB */
trait NewYorkTimesArticleImplicits
  extends MongoConstants
  with PreprocessDataImplicits
  with MetaDataImplicits {


  /** Implicit writer for MongoDB. */
  implicit object NewYorkTimesArticleWriter extends BSONDocumentWriter[NewYorkTimesArticle] {

    override def write(nytArticle: NewYorkTimesArticle): BSONDocument = {
      BSONDocument(
        NYTArticleConstants.UrlString -> BSONString(nytArticle.url),
        NYTArticleConstants.AuthorString -> BSONString(nytArticle.author),
        NYTArticleConstants.TitleString -> BSONString(nytArticle.title),
        NYTArticleConstants.SummariesString -> nytArticle.summaries,
        NYTArticleConstants.KeyPeopleString -> nytArticle.keyPeople,
        NYTArticleConstants.BodyString -> BSONString(nytArticle.body),
        GlobalConstants.MetaDatasString -> nytArticle.metaDatas,
        GlobalConstants.PreprocessDataString -> nytArticle.preprocessData
      )
    }
  }


  implicit object NewYorkTimesArticleReader extends BSONDocumentReader[NewYorkTimesArticle] {

    override def read(doc: BSONDocument): NewYorkTimesArticle = {
      val url = doc.getAs[String](NYTArticleConstants.UrlString).get
      val author = doc.getAs[String](NYTArticleConstants.AuthorString).getOrElse("")
      val title = doc.getAs[String](NYTArticleConstants.TitleString).getOrElse("")
      val summaries = doc.getAs[Set[String]](NYTArticleConstants.SummariesString).getOrElse(Set())
      val keyPeople = doc.getAs[Set[String]](NYTArticleConstants.KeyPeopleString).getOrElse(Set())
      val body = doc.getAs[String](NYTArticleConstants.BodyString).get
      val metaData = doc.getAs[Set[MetaData]](GlobalConstants.MetaDatasString).get
      val preprocessData = doc.getAs[PreprocessData](GlobalConstants.PreprocessDataString).get

      NewYorkTimesArticle(
        url = url,
        author = author,
        title = title,
        summaries = summaries,
        keyPeople = keyPeople,
        body = body,
        metaDatas = metaData,
        preprocessData = preprocessData
      )
    }
  }
}
