package com.baizhi;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ls
 * @date 2021/4/30 - 23:08
 */
public class LuceneHelloWorld {

    /**
     * lucene的索引创建
     */
    @Test
    public void testCreateIndex() throws IOException {
        // 创建一个索引目录
        FSDirectory dir = FSDirectory.open(new File("./index"));
        // 创建索引的配置对象
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
        // 参数1：lucene版本  参数2：分词器对象
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_44,analyzer); // 标准分词器(单字分词器)
        // 创建写出的对象
        // 参数1：索引写出位置  参数2：写出的配置对象
        IndexWriter indexWriter = new IndexWriter(dir,conf);

        // 对一个文章创建索引
        // 创建一个文档对象
        /*for (int i = 0; i < 10; i++) {
            Document document = new Document();
            document.add(new StringField("id",String.valueOf(i), Field.Store.YES));
            document.add(new StringField("title","我是一颗小白杨", Field.Store.YES));
            document.add(new StringField("create",new SimpleDateFormat("yyyy-MM-dd").format(new Date()), Field.Store.YES));
            document.add(new StringField("author","李白", Field.Store.YES));
            document.add(new TextField("content","我是一颗小白杨,你会见我吗？,站在风雨中等你。", Field.Store.YES));
            indexWriter.addDocument(document);
        }*/
        Document document = new Document();
        // 注意: IntField DoubleField FloatFiled...八种基本数据类型 + StringField 不分词
        document.add(new StringField("id","11", Field.Store.YES));
        // 是否在元数据区存放内容 Field.Store.YES
        document.add(new StringField("title","我是一颗小白杨", Field.Store.YES));
        document.add(new StringField("create",new SimpleDateFormat("yyyy-MM-dd").format(new Date()), Field.Store.YES));
        document.add(new StringField("author","李白", Field.Store.YES));
        // TextField会根据指定的分词器分词
        document.add(new TextField("content","我是一颗小白杨,站在风雨中,等等等等等你!!!!", Field.Store.YES));
        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();
    }

    // 索引的搜索
    @Test
    public void TestIndexSearch() throws IOException {
        // 创建索引的位置
        FSDirectory dir = FSDirectory.open(new File("./index"));
        // 读取索引库的位置
        DirectoryReader directoryReader = DirectoryReader.open(dir);
        // 创建索引读取对象
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        // 搜索
        // 参数1：搜索条件  参数2：要所有结果的前多少条
        TopDocs topDocs = indexSearcher.search(new TermQuery(new Term("content", "等")), 100);
        // 相关度排序
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = 0; i < scoreDocs.length; i++) {
            System.out.println("分数:"+scoreDocs[i].score);
            // 文章在索引库中唯一的标识
            int doc = scoreDocs[i].doc;
            // 定位文章
            Document document = indexSearcher.doc(doc);
            System.out.println("1:"+document.get("id"));
            System.out.println("2:"+document.get("title"));
            System.out.println("3:"+document.get("create"));
            System.out.println("4:"+document.get("author"));
            System.out.println("5:"+document.get("content"));
            System.out.println("------------------");
        }
    }

    /**
     * 删除索引库
     * @throws IOException
     */
    @Test
    public void deleteIndex() throws IOException {
        // 创建索引目录
        FSDirectory fsDirectory = FSDirectory.open(new File("./index"));
        // 创建索引的配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, new StandardAnalyzer(Version.LUCENE_44));
        // 创建索引删除操作对象
        IndexWriter indexWriter = new IndexWriter(fsDirectory, config);

        // 删除索引
        // 删除参数1： 删除条件
        indexWriter.deleteDocuments(new Term("id","11"));

        // 提交事务
        indexWriter.commit();

        indexWriter.close();
    }

    /**
     * 清空整个索引库
     *
     */
    @Test
    public void testFlushAll() throws IOException {
        // 创建索引目录
        FSDirectory fsDirectory = FSDirectory.open(new File("./index"));
        // 创建索引的配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, new StandardAnalyzer(Version.LUCENE_44));
        // 创建索引删除操作对象
        IndexWriter indexWriter = new IndexWriter(fsDirectory, config);
        // 删除整个索引库
        indexWriter.deleteAll();
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * 更新索引库中的数据
     *
     * 执行的是先删或插入的策略
     */
    @Test
    public void testUpdateIndex() throws IOException {
        // 创建索引目录
        FSDirectory fsDirectory = FSDirectory.open(new File("./index"));
        // 创建索引的配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, new StandardAnalyzer(Version.LUCENE_44));
        // 创建索引操作对象
        IndexWriter indexWriter = new IndexWriter(fsDirectory, config);
        // 创建一个新的lucene文档
        Document document = new Document();
        document.add(new StringField("title","我是小明", Field.Store.YES));
        document.add(new StringField("create",new SimpleDateFormat("yyyy-MM-dd").format(new Date()), Field.Store.YES));
        document.add(new StringField("author","李白", Field.Store.YES));
        // TextField会根据指定的分词器分词
        document.add(new TextField("content","我是小明明等", Field.Store.YES));
        indexWriter.updateDocument(new Term("id","11"),document);
        indexWriter.commit();
        indexWriter.close();
    }



}
