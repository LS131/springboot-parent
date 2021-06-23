package com.baizhi.test;

import com.baizhi.utils.LuceneUtils;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.highlight.*;
import org.junit.Test;

import java.io.IOException;

import static com.baizhi.utils.LuceneUtils.*;

/**
 * @author ls
 * @date 2021/5/2 - 8:50
 */
public class TestLucenePage {
    @Test
    public void test() throws IOException, ParseException, InvalidTokenOffsetsException {
        Integer pageNow = 3;
        Integer pageSize = 2;
        IndexSearcher indexSearcher = getIndexSearcher();
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(LuceneUtils.version, new String[]{"content"}, LuceneUtils.analyzer);
        Query query = multiFieldQueryParser.parse("天");
        TopDocs topDocs = indexSearcher.search(query, pageNow*pageSize);
        int totalHits = topDocs.totalHits;
        System.out.println("索引库中的总记录:"+totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        int end = topDocs.totalHits < pageNow*pageSize?topDocs.totalHits:pageNow*pageSize;
        for (int i = (pageNow-1) * pageSize; i < end;i++) {
            System.out.println("文章的UID:"+scoreDocs[i].doc);
            System.out.println("分数:"+scoreDocs[i].score);
            Document document = indexSearcher.doc(scoreDocs[i].doc);
            System.out.println(document.get("id"));
            System.out.println(document.get("title"));
            System.out.println(document.get("content"));
            System.out.println(document.get("author"));
            System.out.println(document.get("url"));
            System.out.println("===============");

        }


    }


}
