# Fast JSONL formatter plugin for Embulk

Format json as 1 json in single line.

## Overview

* **Plugin type**: formatter

## Configuration

- **encoding**: output encoding. must be one of "UTF-8", "UTF-16LE", "UTF-32BE", "UTF-32LE" or "UTF-32BE" (string default: 'UTF-8')
- **newline**: newline character. (string default: 'LF')
    - CRLF, LF, CR
- **default_timezone**: timezone. "JST" (string default: UTC)
- **default_timestamp_format**: date format,. (string default: '%Y-%m-%d %H:%M:%S.%6N %z')
- **explode_json_columns**: json column's explode to top fields. (array default:[])
- **json_columns**:  json formatted as string column name. (array default:[])

### additional mode
- **suffix_key**: append suffix of key and value.(map Map[String,String]: {} )

## Example

```yaml
out:
  type: any output input plugin type
  formatter:
    type: fast_jsonl
    explode_json_columns: 
      - JSON_COLUMN_1
      - JSON_COLUMN_2
    suffix_key:
     append_key: append_value
```

## Run Examples

```
./gradlew classpath
embulk run example/config.yml -Ilib
```

## Build

```
$ ./gradlew gem  # -t to watch change of files and rebuild continuously
```
