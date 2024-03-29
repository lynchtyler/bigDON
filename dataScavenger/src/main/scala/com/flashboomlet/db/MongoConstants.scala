package com.flashboomlet.db


trait MongoConstants {

  // DATABASE CONFIGURATION VALUES //

  /* String constant for connecting to MongoDB */
  final val DatabaseIp = "172.16.238.20"

  /* String constant for the database name */
  final val BigDonDatabaseString = "bigdon"

  // TODO: Collection strings

  /* String constant for the new york times article collection */
  final val NewYorkTimesArticlesCollection = "newyorktimearticles"

  /* String constant for the tweets collection */
  final val TweetsCollection = "tweets"

  /* String constant for the entities collection */
  final val EntitiesCollection = "entities"

  /* String constant for the Twitter Searches collection */
  final val TwitterSearchesCollection = "twittersearches"

  /* String constant for the chart collection */
  final val PollsterDataPointsCollection = "pollsterdatapoints"


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
    final val EntityLastNameString = "entity_last_name"

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
    final val ResultString = "result"

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
    final val TweetIDString = "tweet_id"

    /* String constant for the 'content' field in a MongoDB Counts document */
    final val ContentString = "content"

    /* String constant for the 'followersCount' field in a MongoDB Counts document */
    final val FollowersCountString = "followers_count"

    /* String constant for the 'friendsCount' field in a MongoDB Counts document */
    final val FriendsCountString = "friends_count"

    /* String constant for the 'userID' field in a MongoDB Counts document */
    final val UserIDString = "user_id"

    /* String constant for the 'name' field in a MongoDB Counts document */
    final val NameString = "name"

    /* String constant for the 'screenName' field in a MongoDB Counts document */
    final val ScreenNameString = "screen_name"

    /* String constant for the 'social_datas' field in a MongoDB tweet document */
    final val SocialDatasString = "social_datas"

    /* String constant for the 'country' field in a MongoDB Counts document */
    final val CountryString = "country"

    /* String constant for the 'is_retweet' field in a MongoDB tweet document */
    final val IsRetweetString = "is_retweet"

    /* String constant for the 'parent_tweet_id' field in a MongoDB tweet document */
    final val ParentTweetIdString = "parent_tweet_id"
  }


  object TweetSocialDataConstants {

    /* String constant for the 'retweet_count' field in a MongoDB Counts document */
    final val RetweetCountString = "retweet_count"

    /* String constant for the 'favorite_count' field in a MongoDB Counts document */
    final val FavoriteCountString = "favorite_count"

    /* String constant for the 'fetch_date' field in a mongodb tweet social data document */
    final val FetchedDateString = "fetched_date"
  }

  object TwitterSearchConstants {

    /* String constant for the 'query' field in a MongoDB TwitterSearchDocument */
    val QueryString = "query"

    /* String constant for the 'entity_last_name' field in a MongoDB TwitterSearchDocument */
    val EntityLastNameString = "entity_last_name"

    /* String constant for the 'recent_twitter_id' field in a MongoDB TwitterSearchDocument */
    val RecentTwitterIdString = "recent_twitter_id"
  }

  /**
    * String constants used across the entirity of the MongoDB database
    */
  object PollsterDataPointConstants {

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

    /* String constant for the `meta_datas` field in a MongoDB document */
    final val MetaDatasString = "meta_datas"

    /* String constant for the `preprocess_data` field in a MongoDB document */
    final val PreprocessDataString = "preprocess_data"

    /* String constant for the `_id` field in a MongoDB document */
    final val IdString = "_id"

    final val SetString = "$set"

    final val ElemMatchString = "$elemMatch"
  }
}
