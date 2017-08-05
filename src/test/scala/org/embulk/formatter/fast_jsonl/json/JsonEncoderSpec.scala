package org.embulk.formatter.fast_jsonl.json

import io.circe.Json
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class JsonEncoderSpec extends FlatSpec with Matchers {
  it should "be encode as map" in {
    val testData =
      """
        |{
        |  "salutation" : "Hey",
        |  "person" : {
        |    "name" : "Chris"
        |  },
        |  "exclamationMarks" : 3
        |}
      """.stripMargin
    val sequence = JsonParser(testData)
    val map = new mutable.LinkedHashMap[String, Json]()
    sequence.foreach {
      case ((string, json)) =>
        map.put(string, json)
    }
    val jsonString = JsonEncoder(map).noSpaces
    jsonString should be("{\"salutation\":\"Hey\",\"person\":{\"name\":\"Chris\"},\"exclamationMarks\":3}")
  }

}
