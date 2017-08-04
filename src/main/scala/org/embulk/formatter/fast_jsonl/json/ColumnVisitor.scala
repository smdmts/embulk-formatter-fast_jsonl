package org.embulk.formatter.fast_jsonl.json

import io.circe.Json
import org.embulk.spi.time.TimestampFormatter
import org.embulk.spi.{
  Column,
  PageReader,
  ColumnVisitor => EmbulkColumnVisitor
}

case class ColumnVisitor(reader: PageReader,
                         timestampFormatter: TimestampFormatter,
                         explodeColumns: Seq[String],
                         jsonColumns: Seq[String])
    extends EmbulkColumnVisitor {
  import scala.collection.mutable

  private val record = mutable.Map[Column, Json]()
  private val explodeRecord = mutable.LinkedHashMap[String, Json]()

  override def timestampColumn(column: Column): Unit =
    value(column, reader.getTimestamp).foreach(v =>
      put(column, Json.fromString(timestampFormatter.format(v))))

  override def stringColumn(column: Column): Unit =
    value(column, reader.getString).foreach { v =>
      val columnName = column.getName
      if (jsonColumns.contains(columnName)) {
        if (explodeColumns.contains(columnName)) {
          JsonParser(v).foreach {
            case (key, value) =>
              explodeRecord.put(key, value)
          }
        } else {
          explodeRecord.put(columnName, JsonEncoder(v))
        }
      } else {
        put(column, Json.fromString(v))
      }
    }

  override def longColumn(column: Column): Unit =
    value(column, reader.getLong).foreach(v => put(column, Json.fromBigInt(v)))

  override def doubleColumn(column: Column): Unit =
    value(column, reader.getDouble).foreach(v =>
      put(column, Json.fromBigDecimal(v)))

  override def booleanColumn(column: Column): Unit =
    value(column, reader.getBoolean).foreach(v =>
      put(column, Json.fromBoolean(v)))

  override def jsonColumn(column: Column): Unit = {
    value(column, reader.getJson).foreach { v =>
      if (explodeColumns.contains(column.getName)) {
        JsonParser(v.toString).foreach {
          case (key, value) =>
            explodeRecord.put(key, value)
        }
      } else {
        put(column, Json.fromString(v.toJson))
      }
    }
  }

  def value[A](column: Column, method: => (Column => A)): Option[A] =
    if (reader.isNull(column)) {
      None
    } else {
      Some(method(column))
    }

  def put(column: Column, value: Json): Unit = {
    record.put(column, value)
    ()
  }

  def getLine: String = {
    val recordMap = new mutable.LinkedHashMap[String, Json]()
    record.toList
      .sortBy {
        case (column, _) =>
          column.getIndex
      }
      .foreach {
        case (column, json) =>
          recordMap.put(column.getName, json)
      }
    explodeRecord.foreach {
      case (key, json) =>
        recordMap.put(key, json)
    }
    JsonEncoder(recordMap).noSpaces
  }

}
