package com.baizhi.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

/**
 * @author ls
 * @date 2021/5/2 - 8:17
 */
public class TestAnalyzer {

    /**
     * 查看当前分词器的停用词
     */
    @Test
    public void stop(){
        StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_44);
        System.out.println(standardAnalyzer.getStopwordSet());
    }

    /**
     * 标准分词器
     */
    @Test
    public void test(){


    }


}
