package org.embulk.formatter.fast_jsonl.json

import io.circe._
import io.circe.syntax._

import scala.collection.mutable

object JsonEncoder {
  def apply(value: mutable.LinkedHashMap[String, Json]): Json =
    Json.fromFields(value)

  def apply(value: String): Json = {
    value.asJson
  }
}
