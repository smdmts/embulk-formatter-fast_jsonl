package org.embulk.formatter.fast_jsonl.json

import io.circe._
import io.circe.syntax._

object JsonEncoder {
  def apply(value: Map[String, Json]): String = {
    value.asJson.noSpaces
  }
}
