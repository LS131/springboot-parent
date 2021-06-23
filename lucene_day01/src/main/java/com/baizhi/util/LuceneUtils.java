package com.baizhi.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import java.io.File;
import java.io.IOException;

/**
 * @author ls
 * @date 2021/5/1 - 9:53
 *
 * lucene工具类的封装
 */
public class LuceneUtils {

    private static FSDirectory fsDirectory;
    private static IndexWriterConfig indexWriterConfig;
    private static Version version;
    private static Analyzer analyzer;
    private static final ThreadLocal<IndexWriter> t = new ThreadLocal<IndexWriter>();
    private static DirectoryReader directoryReader;

    static {
        try {
            // 创建索引目录
            version = Version.LUCENE_44;
            fsDirectory =  FSDirectory.open(new File("./index"));
            analyzer = new StandardAnalyzer(Version.LUCENE_44);
            // 创建索引的配置对象
            indexWriterConfig = new IndexWriterConfig(version,analyzer);
            // 读取索引库的位置
            directoryReader = DirectoryReader.open(fsDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取indexWriter对象
     *
     * threadLocal本地线程绑定
     */
    public static IndexWriter getIndexWriter(){
        IndexWriter indexWriter = t.get();
        if(indexWriter==null){
            try {
                // 创建索引操作对象
                indexWriter = new IndexWriter(fsDirectory,indexWriterConfig);
                // 将IndexWriter对象放入线程
                t.set(indexWriter);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
        return indexWriter;
    }

    /**
     * 提交事务释放资源
     */

    public static void commit(){
        try {
            // 保险起见执行两次  IndexWriter indexWriter = getIndexWriter();   indexWriter = t.get();
            IndexWriter indexWriter = getIndexWriter();
            indexWriter.commit();
            indexWriter.close();
            t.remove();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务释放资源
     */
    public static void rollback(){
        try {
            // 保险起见执行两次  IndexWriter indexWriter = getIndexWriter();   indexWriter = t.get();
            IndexWriter indexWriter = getIndexWriter();
            indexWriter.rollback();
            indexWriter.close();
            t.remove();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取indexSearch对象
     */
    public static IndexSearcher getIndexSearcher(){
        // 创建索引的搜索对象
        return new IndexSearcher(directoryReader);
    }

}
