package com.flashboomlet.db.implicits

import com.flashboomlet.data.MongoConstants
import com.flashboomlet.data.models.Chart
import com.flashboomlet.data.models.MetaData
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONString
import reactivemongo.bson.BSONDouble

/** Implicit readers and writers for the Chart model in MongoDB */
trait ChartImplicits extends MongoConstants with MetaDataImplicits {

  /** Implicit writer for the Chart class */
  implicit object ChartWriter extends BSONDocumentWriter[Chart] {

    override def write(chart: Chart): BSONDocument = {
      BSONDocument(
        ChartConstants.DateString -> BSONString(chart.date),
        ChartConstants.ClintonString -> BSONDouble(chart.clinton),
        ChartConstants.TrumpString -> BSONDouble(chart.trump),
        ChartConstants.OtherString -> BSONDouble(chart.other),
        ChartConstants.UndecidedString -> BSONDouble(chart.undecided),
        GlobalConstants.MetaDataString -> chart.metaData
      )
    }
  }

  /** Implicit reader for Chart class */
  implicit object ChartReader extends BSONDocumentReader[Chart] {

    override def read(doc: BSONDocument): Chart = {
      val date = doc.getAs[String](ChartConstants.DateString).get
      val clinton = doc.getAs[Double](ChartConstants.ClintonString).get
      val trump = doc.getAs[Double](ChartConstants.TrumpString).get
      val other = doc.getAs[Double](ChartConstants.OtherString).get
      val undecided = doc.getAs[Double](ChartConstants.UndecidedString).get
      val metaData = doc.getAs[MetaData](GlobalConstants.MetaDataString).get

      Chart(
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
