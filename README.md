[![Release](https://jitpack.io/v/umjammer/klab-commons-csv.svg)](https://jitpack.io/#umjammer/klab-commons-csv)

# klab-commons-csv

Annotations for CSV

## POJO annotation

エンティティに`@CsvEntity`を設定し、エンティティーのフィールドに`@CsvColumn`を設定し CSV のカラムを定義します。

```java
 @CsvEntity(url = "classpath:test.csv", encoding = "Windows-31J")
 class Foo {
  @CsvColumn(sequence = 0)
  @GeneratedValue // 読み込み時に自動的に id を振る場合
  Integer id;
  @CsvColumn(sequence = 1)
  String column1;
  @CsvColumn(sequence = 2)
  int column2;
  @CsvColumn(sequence = 3)
  @Enumerated(EnumType.ORDINAL)
  SomeEnumType column3;
  @CsvColumn(sequence = 4)
  @Dialectal // ユーザが定義したクラスや、可変長のカラムを扱う場合とか
  UserType notCsvColumn;
 }
```

## Usage

```Java
 List<Foo> result = CsvEntity.Util.read(Foo.class);
```
