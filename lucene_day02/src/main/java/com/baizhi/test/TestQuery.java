package com.baizhi.test;

import com.baizhi.utils.LuceneUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.io.IOException;

/**
 * @author ls
 * @date 2021/5/1 - 14:21
 */
public class TestQuery {
    /**
     * 1 创建索引
     * 2 搜索的过程
     */
    @Test
    public void test() throws IOException {
        IndexWriter indexWriter = LuceneUtils.getIndexWriter();
        Document document = new Document();
        document.add(new StringField("id","1", Field.Store.YES));
        document.add(new StringField("title","中国日报", Field.Store.YES));
        document.add(new StringField("createDate","2020-12-09", Field.Store.YES));
        document.add(new StringField("author","小黑", Field.Store.YES));
        document.add(new StringField("url","http://www.baidu.com", Field.Store.YES));
        TextField content = new TextField("content", "今天发生了一件大事,婷姐要加班了，但我们要看孩子", Field.Store.YES);
        // 用于设置相关度得分的倍数 十倍 不参与分词的域无法设置得分的倍数
        content.setBoost(10F);
        document.add(content);
        document.add(new TextField("des","婷姐加班", Field.Store.YES));
        indexWriter.addDocument(document);
        LuceneUtils.commit();
    }
    // 根据指定的域(字段)查询 TermQuery 关键字 关键词的搜索
    @Test
    public void testQuery() throws IOException {
        testQuery(new TermQuery(new Term("content","天")));
    }

    /**
     * 查询所有
     * @throws IOException
     */
    @Test
    public void testQuery1() throws IOException {
        testQuery(new MatchAllDocsQuery());
    }

    /**
     * 模糊查询 0--2 输入关键字的模糊查询
     * maxEdits must be between 0 and 2
     */
    @Test
    public void testFuzzyQuery() throws IOException {
        testQuery(new FuzzyQuery(new Term("title","XX日报"),2));
    }

    /**
     * queryParser查询
     * 高级查询:把用户输入的值用指定的分词器查 最后用分词后的进行逐个查询
     */
    @Test
    public void testQueryParser() throws ParseException, IOException {
        QueryParser queryParser = new QueryParser(LuceneUtils.version,"content",LuceneUtils.analyzer);
        Query query = queryParser.parse("发生了");
        testQuery(query);
    }

    /**
     * queryParser查询
     * 多字段查询
     * 高级查询:把用户输入的值用指定的分词器查 最后用分词后的进行逐个查询
     */
    @Test
    public void testMultiFieldQueryParser() throws ParseException, IOException {
        String [] fields = {"title","content","des"};
        QueryParser queryParser = new MultiFieldQueryParser(LuceneUtils.version,fields,LuceneUtils.analyzer);
        Query query = queryParser.parse("中国日报");
        testQuery(query);
    }

    // 布尔查询
    // MUST & MUST_NOT !  SHOULD ||
    @Test
    public void testBoolean() throws IOException {
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(new TermQuery(new Term("content","天")),BooleanClause.Occur.MUST);
        booleanQuery.add(new TermQuery(new Term("des","婷")),BooleanClause.Occur.MUST_NOT);
        testQuery(booleanQuery);
    }



    public void testQuery(Query query) throws IOException {
        // 注意 ：相同的查询条件 查询的关键字在文章中出现的次数一样 总字数少的得分高
        // 注意 ：相同的内容 查询的条件不同(字段不同) 得分也会不同
        IndexSearcher indexSearcher = LuceneUtils.getIndexSearcher();
        TopDocs topDocs = indexSearcher.search(query, 100);
        System.out.println("总记录数:"+topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            indexSearcher.doc(doc);
            Document document = indexSearcher.doc(doc);
            System.out.println("得分:"+scoreDoc.score);
            System.out.println(document.get("id"));
            System.out.println(document.get("title"));
            System.out.println(document.get("createDate"));
            System.out.println(document.get("author"));
            System.out.println(document.get("content"));
            System.out.println(document.get("url"));
            System.out.println(document.get("des"));
            System.out.println("----------------------");
        }
    }
}
