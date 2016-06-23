package com.flashboomlet.db.implicits

import java.util.Date

import com.flashboomlet.data.models.PollsterDataPoint
import com.flashboomlet.data.models.MetaData
import com.flashboomlet.db.MongoConstants
import reactivemongo.bson.BSONDateTime
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONDouble

/** Implicit readers and writers for the Chart model in MongoDB */
trait PollsterDataPointImplicits extends MongoConstants with MetaDataImplicits {

  /** Implicit writer for the Chart class */
  implicit object PollsterDataPointWriter extends BSONDocumentWriter[PollsterDataPoint] {

    override def write(chart: PollsterDataPoint): BSONDocument = {
      BSONDocument(
        PollsterDataPointConstants.DateString -> BSONDateTime(chart.date),
        PollsterDataPointConstants.ClintonString -> BSONDouble(chart.clinton),
        PollsterDataPointConstants.TrumpString -> BSONDouble(chart.trump),
        PollsterDataPointConstants.OtherString -> BSONDouble(chart.other),
        PollsterDataPointConstants.UndecidedString -> BSONDouble(chart.undecided),
        GlobalConstants.MetaDatasString -> chart.metaData
      )
    }
  }

  /** Implicit reader for Chart class */
  implicit object PollsterDataPointReader extends BSONDocumentReader[PollsterDataPoint] {

    override def read(doc: BSONDocument): PollsterDataPoint = {
      val date = doc.getAs[Date](PollsterDataPointConstants.DateString).get.getTime
      val clinton = doc.getAs[Double](PollsterDataPointConstants.ClintonString).get
      val trump = doc.getAs[Double](PollsterDataPointConstants.TrumpString).get
      val other = doc.getAs[Double](PollsterDataPointConstants.OtherString).get
      val undecided = doc.getAs[Double](PollsterDataPointConstants.UndecidedString).get
      val metaData = doc.getAs[MetaData](GlobalConstants.MetaDatasString).get

      PollsterDataPoint(
        date = date,
        clinton = clinton,
        trump = trump,
        other = other,
        undecided = undecided,
        metaData = metaData
      )
    }
  }
}
