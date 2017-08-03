package org.embulk.formatter.fast_jsonl.json

import org.scalatest.{FlatSpec, Matchers}

class JsonEncoderSpec extends FlatSpec with Matchers {
  it should "be encode" in {
    val sequence = JsonParser("{\"a\":\"b\", \"c\":\"d\"}")
    val jsonString = JsonEncoder(sequence.toMap)
    jsonString should be("{\"a\":\"b\",\"c\":\"d\"}")
  }
}
