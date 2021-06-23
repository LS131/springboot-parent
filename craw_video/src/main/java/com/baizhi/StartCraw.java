package com.baizhi;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ls
 * @date 2021/5/2 - 18:20
 */
public class StartCraw {

    private static File file =null;

    private static FileOutputStream fileOutputStream =null;
    //线程池
    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        String url = "http://www.6uyy.com/dongman/tianbaofuyaolu/play-0-0.html";
        Connection.Response response = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .ignoreContentType(true)
                .execute();
        String body = response.body();
        System.out.println(body);
        /*String fileName = "";
        Document document = Jsoup.parse(body);
        Elements elements = document.select("li");
        for (Element element : elements) {
            if(element.text().contains("当前播放")){
                fileName = element.text().substring(element.text().indexOf("：")+1,element.text().toString().length());
            }
        }
        file = new File( "E:\\LINGLONGALL\\"+fileName+".mp4");
        fileOutputStream = new FileOutputStream(file);
        Elements script = document.getElementsByTag("script");
        String urlString = "";
        for (DataNode dataNode : script.get(5).dataNodes()){
            urlString = dataNode.getWholeData();
        }
        urlString = urlString.substring(urlString.indexOf("{"),urlString.indexOf("}")+1).replace("\\","");
        JSONObject jsonObject =  JSONObject.parseObject(urlString);
        StartCraw.createM3u8(Objects.toString(jsonObject.get("url")));*/
    }

    private static void createM3u8(String url) throws IOException {
        Connection.Response response = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .ignoreContentType(true)
                .execute();
        byte[] bytes = response.bodyAsBytes();
        ByteArrayInputStream stream = new ByteArrayInputStream(response.bodyAsBytes());
        FileUtils.copyInputStreamToFile(stream,new File("E:\\LINGLONGALL\\index.m3u8"));
        FileReader fr = new FileReader("E:\\LINGLONGALL\\index.m3u8");//参数里面必须放目标文件名，否则会报错
        BufferedReader bufr = new BufferedReader(fr);
        String s = bufr.readLine();
        List<String> urlList = new LinkedList<String>();
        String tempUrl = "";
        while((tempUrl = bufr.readLine())!=null){
            if(tempUrl.startsWith("https://p1.pstatp.com")){
                urlList.add(tempUrl);
            }
            tempUrl = null;
        }
        for (int i = 0; i < urlList.size(); i++) {
            String tsStrUrl = urlList.get(i);
            Connection.Response responseTs = Jsoup.connect(tsStrUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                    .ignoreContentType(true)
                    .execute();
            System.out.println("请求的地址为:"+tsStrUrl);
            byte[] bytesTs = responseTs.bodyAsBytes();
            System.out.println("获取的字节为:"+bytesTs.length);
            if (file.exists())
                file.delete();
            else file.createNewFile();
            fileOutputStream.write(bytesTs, 0, bytesTs.length);
            //ByteArrayInputStream streamTs = new ByteArrayInputStream(responseTs.bodyAsBytes());
            //FileUtils.copyInputStreamToFile(streamTs,new File("E:\\LINGLONGALL\\"+i+".ts"));
        }
        fileOutputStream.close();
        while(fixedThreadPool.isTerminated()){
            fixedThreadPool.shutdown();
        }
        System.out.println("文件下载成功");
    }
}
