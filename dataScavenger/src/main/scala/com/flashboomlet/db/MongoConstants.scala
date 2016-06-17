package com.flashboomlet.data


trait MongoConstants {

  // DATABASE CONFIGURATION VALUES //

  /* String constant for connecting to MongoDB */
  final val LocalHostString = "localhost"

  /* String constant for the database name */
  final val BigDonDatabaseString = "bigdon"

  // TODO: Collection strings

  /* String constant for the new york times article collection */
  final val NewYorkTimesArticlesCollection = "newyorktimearticles"

  /* String constant for the tweets collection */
  final val TweetsCollection = "tweets"

  /* String constant for the entities collection */
  final val EntitiesCollection = "entities"


  /**
    * String constants used in the New York Times Articles collection model schema
    */
  object NYTArticleConstants {

    /* String constant for the `url` field in a MongoDB NewYorkTimesArticle document */
    final val UrlString = "url"

    /* String constant for the `title` field in a MongoDB NewYorkTimesArticle document */
    final val TitleString = "title"

    /* String constant for the `author` field in a MongoDB NewYorkTimesArticle document */
    final val AuthorString = "author"

    /* String constant for the `body` field in a MongoDB NewYorkTimesArticle document */
    final val BodyString = "body"

    /* String constant for the `summaries` field in a MongoDB NewYorkTimesArticle document */
    final val SummariesString = "summaries"

    /* String constant for the `key_people` field in a MongoDB NewYorkTimesArticle document */
    final val KeyPeopleString = "key_people"
  }
  // TODO: TWEETS SCHEMA VALUES


  /**
    * String constants used in the Entities collection model schema
    */
  object EntityConstants {

    /* String constant for the `first_name` field in a MongoDB Entity document */
    final val FirstNameString = "first_name"

    /* String constant for the `last_name` field in a MongoDB Entity document */
    final val LastNameString = "last_name"

    /* String constant for the `party` field in a MongoDB Entity document */
    final val PartyString = "party"

    /* String constant for the `search_terms` field in a MongoDB Entity document */
    final val SearchTermsString = "search_terms"
  }

  /**
    * String constants used in the MetaData model schema
    */
  object MetaDataConstants {

    /* String constant for the `fetch_date` field in a MongoDB MetaData document */
    final val FetchDateString = "fetch_date"

    /* String constant for the `publish_date` field in a MongoDB MetaData document */
    final val PublishDateString = "publish_date"

    /* String constant for the `source` field in a MongoDB MetaData document */
    final val SourceString = "source"

    /* String constant for the `search_term` field in a MongoDB MetaData document */
    final val SearchTermString = "search_term"

    /* String constant for the `entity_id` field in a MongoDB MetaData document */
    final val EntityIdString = "entity_id"

    /* String constant for the `entity_id` field in a MongoDB MetaData document */
    final val ContributionsString = "contributions"

  }

  /**
    * String constants used in the PreprocessData model schema
    */
  object PreprocessDataConstants {

    /* String constant for the `sentiment` field in a MongoDB PreprocessData document */
    final val SentimentString = "sentiment"

    /* String constant for the `counts` field in a MongoDB PreprocessData document */
    final val CountsString = "counts"
  }

  /**
    * String constants used in the PreprocessData model Schema
    */
  object SentimentConstants {

    /* String constant for the `sentiment` field in a MongoDB Sentiment document */
    final val SentimentString = "sentiment"

    /* String constant for the `confidence` field in a MongoDB Sentiment document */
    final val ConfidenceString = "confidence"
  }

  /**
    * String constants used in the Counts model schema
    */
  object CountsConstants {

    /* String constant for the `word_count` field in a MongoDB Counts document */
    final val WordCountString = "word_count"

    /* String constant for the `sentence_count` field in a MongoDB Counts document */
    final val SentenceCountString = "sentence_count"

    /* String constant for the `title_word_count` field in a MongoDB Counts document */
    final val TitleWordCountString = "title_word_count"

    /* String constant for the `search_term_count` field in a MongoDB Counts document */
    final val SearchTermCountString = "search_term_count"

    /* String constant for the 'word_occurrences' field in a MongoDB Counts document */
    final val WordOccurrencesString = "word_occurrences"
  }

  object TwitterConstants {

    /* String constant for the 'tweetID' field in a MongoDB Counts document */
    final val tweetIDString = "tweetID"

    /* String constant for the 'content' field in a MongoDB Counts document */
    final val contentString = "content"

    /* String constant for the 'followersCount' field in a MongoDB Counts document */
    final val followersCountString = "followersCount"

    /* String constant for the 'friendsCount' field in a MongoDB Counts document */
    final val friendsCountString = "friendsCount"

    /* String constant for the 'userID' field in a MongoDB Counts document */
    final val userIDString = "userID"

    /* String constant for the 'name' field in a MongoDB Counts document */
    final val nameString = "name"

    /* String constant for the 'screenName' field in a MongoDB Counts document */
    final val screenNameString = "screenName"

    /* String constant for the 'favoriteCount' field in a MongoDB Counts document */
    final val favoriteCountString = "favoriteCount"

    /* String constant for the 'country' field in a MongoDB Counts document */
    final val countryString = "country"

    /* String constant for the 'retweetCount' field in a MongoDB Counts document */
    final val retweetCountString = "retweetCount"

    /* String constant for the 'metaData' field in a MongoDB Counts document */
    final val metaDataString = "metaData"

    /* String constant for the 'preprocessData' field in a MongoDB Counts document */
    final val preprocessDataString = "preprocessData"
  }

  /**
    * String constants used across the entirity of the MongoDB database
    */
  object ChartConstants {

    /* String constant for the 'date' field in a MongoDB Counts document */
    final val DateString = "date"

    /* String constant for the 'clinton' field in a MongoDB Counts document */
    final val ClintonString = "clinton"

    /* String constant for the 'trump' field in a MongoDB Counts document */
    final val TrumpString = "trump"

    /* String constant for the 'other' field in a MongoDB Counts document */
    final val OtherString = "other"

    /* String constant for the 'undecided' field in a MongoDB Counts document */
    final val UndecidedString = "undecided"

  }

  /**
    * String constants used across the entirity of the MongoDB database
    */
  object GlobalConstants {

    /* String constant for the `meta_data` field in a MongoDB document */
    final val MetaDataString = "meta_data"

    /* String constant for the `preprocess_data` field in a MongoDB document */
    final val PreprocessDataString = "preprocess_data"

    /* String constant for the `_id` field in a MongoDB document */
    final val IdString = "_id"
  }
}
