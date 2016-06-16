package com.flashboomlet.scavenger.twitter.twitterResponses

import java.util.Date

import com.danielasfregola.twitter4s.entities.GeoPlace
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


/**
  * Case class wrapping the tweets
  *
  * @param shortTweets The sequence of tweets
  */
@JsonIgnoreProperties(Array(
  "contributors",
  "current_user_retweet",
  "entities",
  "favorited",
  "filter_level",
  "id_str",
  "in_reply_to_screen_name",
  "in_reply_to_status_id",
  "in_reply_to_status_id_str",
  "in_reply_to_user_id",
  "in_reply_to_user_id_str",
  "is_quote_status",
  "lang",
  "possibly_sensitive",
  "quoted_status_id",
  "quoted_status_id_str",
  "quoted_status",
  "scopes",
  "retweeted",
  "retweeted_status",
  "source",
  "truncated",
  "withheld_copyright",
  "withheld_in_countries",
  "withheld_scope",
  "metadata"))
case class Response(shortTweets: Set[ShortTweetResponse])

/**
  * Case class of the tweet
  *
  * @param id if of the tweet
  * @param text the content of the tweet
  * @param user the user data
  * @param coordinates the coordinates of where the tweet was tweeted
  * @param created_at the date of creation of the tweet
  * @param favorite_count the amount of favorites the tweet has gotten
  * @param place is an object that holds metadata on the location of where the tweet was tweeted
  * @param retweet_count the amount of retweets the tweet has gotten
  */
@JsonIgnoreProperties(Array(
"contributors",
"current_user_retweet",
"entities",
"favorited",
"filter_level",
"id_str",
"in_reply_to_screen_name",
"in_reply_to_status_id",
"in_reply_to_status_id_str",
"in_reply_to_user_id",
"in_reply_to_user_id_str",
"is_quote_status",
"lang",
"possibly_sensitive",
"quoted_status_id",
"quoted_status_id_str",
"quoted_status",
"scopes",
"retweeted",
"retweeted_status",
"source",
"truncated",
"withheld_copyright",
"withheld_in_countries",
"withheld_scope",
"metadata"))
  case class ShortTweetResponse(
  id: Long,
  text: String,
  user: Option[ShortUserResponse] = None,
  coordinates: Seq[Seq[Seq[Double]]] = Seq.empty,
  created_at: Date,
  favorite_count: Int = 0,
  place: Option[GeoPlace] = None,
  retweet_count: Long = 0
)

/**
  * Case class of the user data
  *
  * @param followers_count the amount of followers that a user has
  * @param friends_count the amount of friends a user is following
  * @param id the user's ID number
  * @param name the user's current panhandle
  * @param screen_name the user's current display name
  */
@JsonIgnoreProperties(Array(
  "blocked_by",
  "blocking",
  "contributors_enabled",
  "created_at",
  "default_profile",
  "default_profile_image",
  "description",
  "email",
  "entities",
  "favourites_count",
  "follow_request_sent",
  "following",
  "geo_enabled",
  "has_extended_profile",
  "id_str",
  "is_translation_enabled",
  "is_translator",
  "lang",
  "listed_count",
  "location",
  "muting",
  "notifications",
  "profile_background_color",
  "profile_background_image_url",
  "profile_background_image_url_https",
  "profile_background_tile",
  "profile_banner_url",
  "profile_image_url",
  "profile_image_url_https",
  "profile_link_color",
  "profile_sidebar_border_color",
  "profile_sidebar_fill_color",
  "profile_text_color",
  "profile_use_background_image",
  "protected",
  "show_all_inline_media",
  "status",
  "statuses_count",
  "time_zone",
  "url",
  "utc_offset",
  "withheld_in_countries",
  "withheld_scope"
))
case class ShortUserResponse(
  followers_count: Int,
  friends_count: Int,
  id: Long,
  name: String,
  screen_name: String
)
