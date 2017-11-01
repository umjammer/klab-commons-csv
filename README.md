klab-commons-csv
================

# CSV を扱うアノテーションライブラリ

エンティティに`@CsvEntity`を設定し、エンティティーのフィールドにも`@CsvColumn`を設定し CSV のカラムを定義します。
エンティティはジェネリックな`CsvDao`から利用することができます。
SpringFramework 等でサービスに注入すれば [persistence DAO](https://github.com/umjammer/vavi-commons/tree/master/vavi-persistence) と同等に使用することが可能になります。
制限は、まだ JPA ほどの行操作 API はありません。`CsvDao#findAll()` 全取得、 `CsvDao#updateAll(Collection)` 全書き出しのみです。

## SpringFramework を使用した例

### 基本設定
spring-beans.xml に
```xml
   <bean id="csvDaoBase"
         class="org.klab.commons.csv.dao.CsvDaoBase"
         abstract="true">
   </bean>
```
と設定します。

### 使用する側

CSV をデータソースとして使用するためのラッパ`CsvFactory`の実装を作成します。基本実装としてリソースを扱う`ResourceCsvFactory`とファイルを扱う`FileCsvFactory`、 ストリームを扱う`IOStreamCsvFactory`が用意されています。

`CsvDaoBase`に`CsvEntity`実装クラスと`CsvFactory`の実装を設定した物を`fooCsvDao`と定義します。

POJO annotation

```java
 @CsvEntity
 class Foo implements org.klab.commons.csv.dao.CsvEntity {
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

spring-beans.xml
```xml
    <bean id="fooCsvDao"
          parent="csvDaoBase">
      <property name="entityClass"
                value="your.package.foo.impl.FooImpl" />
      <property name="csvFactory"
                ref="csvFactory" />
    </bean>
    <bean id="csvFactory"
          class="org.klab.commons.csv.impl.FileCsvFactory">
      <property name="fileName"
                value="/sample/sample.csv" />
    </bean>
```

あとは fooCsvDao をサービスに注入すれば [persistence DAO] (http://code.google.com/p/vavi-commons/source/browse/trunk/vavi-persistence/) と
ほぼ同様に使用することが可能になります。

```Java
 class FooServiceImpl extends FooService {
  CsvDao<Foo, Integer> csvDao;
  public void setFooCsvDao(CsvDao<Foo, Integer> csvDao) {
   this.csvDao = csvDao;
  }
  public List<Foo> findAll() {
   // CSV 取ってくるだけならたった１行！
   return csvDao.findAll();
  }
 }
```

## 普通に使用した例

```Java
 CsvDaoBase<Foo, Integer> csvDao = new CsvDaoBase<Foo, Integer>();
 csvDao.setEntityClass(FooImpl.class);
 FileCsvFactory csvFactory = new FileCsvFactory();
 csvFactory.setFileName("/sample/sample.csv");
 csvDao.setCsvFactory(csvFactory);

 List<Foo> result = csvDao.findAll();
```

## Android

```Java
 CsvDaoBase<Foo, Integer> csvDao = new CsvDaoBase<Foo, Integer>();
 csvDao.setEntityClass(FooImpl.class);
 IOStreamCsvFactory csvFactory = new IOStreamCsvFactory();
 Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.bar);
 csvFactory.setInputStream(getContentResolver().openInputStream(uri));
 csvDao.setCsvFactory(csvFactory);
        
 List<Foo> result = csvDao.findAll();
```
