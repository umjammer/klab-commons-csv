[![Release](https://jitpack.io/v/umjammer/klab-commons-csv.svg)](https://jitpack.io/#umjammer/klab-commons-csv)
[![Java CI](https://github.com/umjammer/klab-commons-csv/workflows/Java%20CI/badge.svg)](https://github.com/umjammer/klab-commons-csv/actions)
[![CodeQL](https://github.com/umjammer/klab-commons-csv/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/umjammer/klab-commons-csv/actions/workflows/codeql-analysis.yml)
![Java](https://img.shields.io/badge/Java-8-b07219)

# klab-commons-csv

Annotations for CSV

## POJO annotation

Just set `@CsvEntity` to POJO, `@CsvColumn` to POJO's field as CSV column definitions.

```java
 @CsvEntity(url = "classpath:test.csv", encoding = "Windows-31J")
 class Foo {
  @CsvColumn(sequence = 0)
  @GeneratedValue // give a id number automatically when reading.
  Integer id;
  @CsvColumn(sequence = 1)
  String column1;
  @CsvColumn(sequence = 2)
  int column2;
  @CsvColumn(sequence = 3)
  @Enumerated(EnumType.ORDINAL)
  SomeEnumType column3;
  @CsvColumn(sequence = 4)
  @Dialectal // dealing user defined columns or variable length columns
  UserType notCsvColumn;
 }
```

## Usage

```Java
 List<Foo> result = CsvEntity.Util.read(Foo.class);
```
