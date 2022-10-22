# org.klab.commons.csv

CSV のユーティリティです。`commons-persistence` と JPA の設計を
まねて作られています。わずらわしいフィールドコピーの単純コード
からおサラバです。

エンティティに `CsvEntity` アノテーションを設定し、
エンティティーのフィールドにも `CsvColumn` アノテーションを設定し CSV の
カラムを定義します。エンティティはジェネリックな `CsvDao` DAO から
利用することができます。SpringFramework 等でサービスに注入すれば `commons-persistence`
の DAO と同等に使用することが可能になります。制限は、まだ JPA ほどの行操作 API は
ありません。`CsvDao#findAll()` 全取得、`CsvDao#updateAll(Collection)` 全書き出しのみです。

## SpringFramework を使用した例

### 基本設定

`spring-beans.xml` に

```xml
   &lt;bean id=&quot;csvDaoBase&quot;
   class=&quot;org.klab.commons.util.csv.impl.CsvDaoBase&quot;
   abstract=&quot;true&quot;&gt;
   &lt;/bean&gt;
```
と設定します。

### 使用する側

CSV をデータソースとして使用するためのラッパ `CsvFactory` の実装を
作成します。基本実装としてリソースを扱う `ResourceCsvFactory` と
ファイルを扱う `FileCsvFactory`、
ストリームを扱う `IOStreamCsvFactory` が用意されています。

`CsvDaoBase` に `CsvEntity` 実装クラスと `CsvFactory` の実装を
設定した物を `fooCsvDao` と定義します。

 * POJO annotation

```java
 @CsvEntity
 Foo implements CsvEntity {
  @CsvColumn(sequence = 0)
  @GeneratedValue // 読み込み時に自動的に id を振る場合
  Integer id;
  @CsvColumn(sequence = 1)
  String column1;
  @CsvColumn(sequence = 2)
  int column2;
  @CsvColumn(sequence = 3)
  @Enumerated // JPA の Enumerated と同じ仕様
  EnumType column3;
  @CsvColumn(sequence = 4)
  @Dialectal // ユーザが定義したクラスや、可変長のカラムを扱う場合とか
  UserType notCsvColumn;
 }
```

 * `spring-beans.xml`

```xml
    &lt;bean id=&quot;fooCsvDao&quot;
          parent=&quot;csvDaoBase&quot;&gt;
      &lt;property name=&quot;entityClass&quot;
                value=&quot;your.package.foo.impl.FooImpl&quot; /&gt;
      &lt;property name=&quot;csvDataSource&quot;
                ref=&quot;csvDataSource&quot; /&gt;
    &lt;/bean&gt;
    &lt;bean id=&quot;csvDataSource&quot;
          class=&quot;org.klab.commons.util.csv.impl.FileCsvFactory&quot;&gt;
      &lt;property name=&quot;fileName&quot;
                value=&quot;/sample/sample.csv&quot; /&gt;
    &lt;/bean&gt;
```

あとは `fooCsvDao` をサービスに注入すれば `commons-persistence` DAO と
ほぼ同様に使用することが可能になります。

```java
 class FooServiceImpl extends FooService {
  CsvDao<foo, integer> csvDao;
  public void setFooCsvDao(CsvDao<foo, integer> csvDao) {
   this.csvDao = csvDao;
  }
  public List<foo> findAll() {
   // CSV 取ってくるだけならたった１行！
   return csvDao.findAll();
  }
 }
```

## TODO

 * ~~可変長のカラム~~
 * ~~このプロジェクトもそうやんけ！~~
 * [敵](http://ykhr-kokko.sourceforge.jp/cgi-bin/wiki.cgi?page=Choco+CSVUtil)がすでにいた
