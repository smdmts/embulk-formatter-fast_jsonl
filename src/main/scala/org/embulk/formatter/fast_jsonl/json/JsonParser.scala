package org.embulk.formatter.fast_jsonl.json

import io.circe.Json
import io.circe.parser._

object JsonParser {
  def apply(value: String): Seq[(String, Json)] =
    decode[Map[String, Json]](value) match {
      case Right(v: Map[String, Json]) =>
        v.toIterator.toSeq
      case _ =>
        sys.error(s"could not parse json. $value")
    }

  def toJson(value: String): Json = {
    parse(value) match {
      case Right(v) =>
        v
      case _ =>
        sys.error(s"could not parse json. $value")
    }
  }
}
