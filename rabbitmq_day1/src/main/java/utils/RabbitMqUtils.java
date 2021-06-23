package utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * mq工具类
 * @author ls
 * @date 2021/6/6 - 12:25
 *
 */
public class RabbitMqUtils {
    private static ConnectionFactory connectionFactory;// 定义链接mq的连接工厂对象

    static {
        // 创建链接mq的连接工厂对象（重量级的资源）
        connectionFactory = new ConnectionFactory();// 创建链接mq的连接工厂对象
        connectionFactory.setHost("192.168.152.134"); // 设置链接mq的主机
        connectionFactory.setPort(5672); //设置端口号
        connectionFactory.setVirtualHost("/ems");//设置链接那个虚拟主机
        connectionFactory.setUsername("ems");//设置用户名
        connectionFactory.setPassword("123");//设置密码
    }

    //定义提供创建链接对象的方法
    public static Connection getConnection() throws IOException, TimeoutException {
        try{
            Connection connection = connectionFactory.newConnection();//获取链接对象
            return connection;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //关闭通道和关闭链接工具方法
    public static void closeConnectionAndChanel(Channel channel,Connection connection){
        try{
            if(channel!=null)channel.close();//关闭通道 不关闭的话就一直处于监听状态
            if(connection!=null)connection.close();// 关闭连接
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
