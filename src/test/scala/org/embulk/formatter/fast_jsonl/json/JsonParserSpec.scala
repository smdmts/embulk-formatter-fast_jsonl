package org.embulk.formatter.fast_jsonl.json

import org.scalatest._

class JsonParserSpec extends FlatSpec with Matchers {
  it should "be parse" in {
    val sequence = JsonParser("{\"a\":\"b\", \"c\":\"d\"}")
    sequence.size should be(2)
  }
}
