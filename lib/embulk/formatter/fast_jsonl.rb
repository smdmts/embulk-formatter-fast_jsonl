Embulk::JavaPlugin.register_formatter(
    "fast_jsonl", "org.embulk.formatter.fast_jsonl.FastJsonlFormatterPlugin",
    File.expand_path('../../../../classpath', __FILE__))