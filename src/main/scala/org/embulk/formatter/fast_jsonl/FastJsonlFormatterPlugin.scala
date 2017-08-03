package org.embulk.formatter.fast_jsonl

import java.nio.charset.{Charset, StandardCharsets}

import org.embulk.config.{ConfigSource, TaskSource}
import org.embulk.spi._

class FastJsonlFormatterPlugin extends FormatterPlugin {

  override def transaction(config: ConfigSource,
                           schema: Schema,
                           control: FormatterPlugin.Control): Unit = {
    val task = config.loadConfig(classOf[PluginTask])
    validateCharset(task)
    control.run(task.dump())
  }

  def validateCharset(task: PluginTask): Unit =
    task.getCharset match {
      case v if v == StandardCharsets.UTF_8 =>
      case v if v == StandardCharsets.UTF_16BE =>
      case v if v == StandardCharsets.UTF_16LE =>
      case v if v == StandardCharsets.UTF_16 =>
      case v if v == Charset.forName("UTF-32") =>
      case v if v == Charset.forName("UTF-32BE") =>
      case v if v == Charset.forName("UTF-32LE") =>
      case _ =>
        sys.error("unmatch json character set.")
    }

  override def open(taskSource: TaskSource,
                    schema: Schema,
                    output: FileOutput): PageOutput = {
    val task = taskSource.loadTask(classOf[PluginTask])
    PageOutput(schema, task, output)
  }

}
