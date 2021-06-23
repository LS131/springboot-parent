package workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 任务模型(默认是平均分配的)
 * @author ls
 * @date 2021/6/6 - 14:40
 */
public class Provider {
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取链接对象
        Connection connection = RabbitMqUtils.getConnection();
        // 获取通道对象
        Channel channel = connection.createChannel();
        //通过通道声明队列
        channel.queueDeclare("work",true,false,false,null);
        //生成消息
        for (int i = 0; i < 20; i++) {
            channel.basicPublish("","work",null,(String.valueOf(i)+"hello word quece").getBytes());
        }
        //关闭资源
        RabbitMqUtils.closeConnectionAndChanel(channel,connection);
    }
}
