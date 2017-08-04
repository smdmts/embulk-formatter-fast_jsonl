package org.embulk.formatter.fast_jsonl.json

import io.circe._
import io.circe.syntax._

object JsonEncoder {
  def apply(value: Map[String, Json]): Json = {
    value.asJson
  }

  def apply(value: String): Json = {
    value.asJson
  }
}
