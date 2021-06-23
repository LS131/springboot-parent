package helloword;

import com.rabbitmq.client.*;
import org.junit.Test;
import utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ls
 * @date 2021/6/6 - 12:04
 */
public class Customer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取链接对象
        Connection connection = RabbitMqUtils.getConnection();
        // 获取连接中的通道对象
        Channel channel = connection.createChannel();
        //通道去绑定对应的消息队列
        // 参数1：队列名 不存在的话自动创建
        // 参数2：用来定义队列的特性 true 持久化队列 false 不持久化
        // 参数3：exclusive 是否独占队列 true 独占队列（仅当前链接可用的队列） false 不独占队列
        // 参数4：autoDelete 是否载消费完成后自动删除队列 true 自动删除 false 不自动删除
        // 参数5：额外附加的参数
        channel.queueDeclare("hello",true,false,false,null);

        //消费消息
        //参数2：开启消息的自动确认机制
        //参数3：消费时的回调接口
        channel.basicConsume("hello",true,new DefaultConsumer(channel){
            @Override // 最后的参数：消息队列中取出的消息
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("=======================:"+new String(body));
            }
        });
        //不关闭的话就一直处于监听状态
        //channel.close();
        //connection.close();
    }
}
