package org.embulk.formatter.fast_jsonl

import com.google.common.base.Optional
import scala.collection.JavaConverters._
import org.embulk.formatter.fast_jsonl.json.ColumnVisitor
import org.embulk.spi.{
  FileOutput,
  Page,
  PageReader,
  Schema,
  PageOutput => EmbulkPageOutput
}
import org.embulk.spi.time.TimestampFormatter
import org.embulk.spi.util.LineEncoder

case class PageOutput(schema: Schema, task: PluginTask, output: FileOutput)
    extends EmbulkPageOutput {
  val encoder = new LineEncoder(output, task)
  val reader: PageReader = new PageReader(schema)
  val explodeColumns: Seq[String] = task.getExplodeJsonColumns().asScala
  val jsonColumns: Seq[String] = task.getJsonColumns().asScala
  val suffixKey: Map[String, String] = task.getSuffixKey().asScala.toMap
  private var opened: Boolean = false

  val timestampFormatter: TimestampFormatter =
    new TimestampFormatter(task, Optional.absent())

  override def add(page: Page): Unit = {
    if (!opened) {
      encoder.nextFile()
      opened = true
    }
    val reader: PageReader = new PageReader(schema)
    reader.setPage(page)
    while (reader.nextRecord()) {
      val visitor =
        ColumnVisitor(reader,
                      timestampFormatter,
                      explodeColumns,
                      jsonColumns,
                      suffixKey)
      schema.visitColumns(visitor)
      encoder.addLine(visitor.getLine)
    }
    ()
  }

  override def finish(): Unit = encoder.finish()
  override def close(): Unit = encoder.finish()

}
