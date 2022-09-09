<p>
CSV のユーティリティです。commons-persistence と JPA の設計を
まねて作られています。わずらわしいフィールドコピーの単純コード
からおサラバです。
</p>
<p>
エンティティに{@link CsvEntity アノテーション}を設定し、
エンティティーのフィールドにも{@link CsvColumn アノテーション}を設定し CSV の
カラムを定義します。エンティティはジェネリックな{@link CsvDao DAO}から
利用することができます。SpringFramework 等でサービスに注入すれば commons-persistence
の DAO と同等に使用することが可能になります。制限は、まだ JPA ほどの行操作 API は
ありません。{@link CsvDao#findAll() 全取得}、{@link CsvDao#updateAll(Collection) 全書き出し}のみです。
</p>

<h2>SpringFramework を使用した例</h2>

<h3>基本設定</h3>
spring-beans.xml に
<pre>
   &lt;bean id=&quot;csvDaoBase&quot;
   class=&quot;org.klab.commons.util.csv.impl.CsvDaoBase&quot;
   abstract=&quot;true&quot;&gt;
   &lt;/bean&gt;
</pre>
と設定します。

<h3>使用する側</h3>

<p>
CSV をデータソースとして使用するためのラッパ {@link CsvFactory} の実装を
作成します。基本実装としてリソースを扱う {@link ResourceCsvFactory} と
ファイルを扱う {@link FileCsvFactory}、 
ストリームを扱う {@link IOStreamCsvFactory} が用意されています。
</p>
<p>
{@link CsvDaoBase} に {@link CsvEntity} 実装クラスと {@link CsvFactory} の実装を
設定した物を fooCsvDao と定義します。
</p>

POJO annotation

<pre>
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
</pre>

spring-beans.xml
<pre>
    &lt;bean id=&quot;fooCsvDao&quot;
          parent=&quot;csvDaoBase&quot;&gt;
      &lt;property name=&quot;entityClass&quot;
                value=&quot;your.package.foo.impl.FooImpl&quot; /&gt;
      &lt;property name=&quot;csvFactory&quot;
                ref=&quot;csvFactory&quot; /&gt;
    &lt;/bean&gt;
    &lt;bean id=&quot;csvFactory&quot;
          class=&quot;org.klab.commons.util.csv.impl.FileCsvFactory&quot;&gt;
      &lt;property name=&quot;fileName&quot;
                value=&quot;/sample/sample.csv&quot; /&gt;
    &lt;/bean&gt;
</pre>

<p>
あとは fooCsvDao をサービスに注入すれば commons-persistence DAO と
ほぼ同様に使用することが可能になります。
</p>

<pre>
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
</pre>

<h3>TODO</h3>

<ul>
 <li><del>可変長のカラム</del></li>
 <li><del>このプロジェクトもそうやんけ！</del></li>
 <li><a href="http://ykhr-kokko.sourceforge.jp/cgi-bin/wiki.cgi?page=Choco+CSVUtil">敵</a>がすでにいた</li>
</ul>