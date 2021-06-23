package topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ls
 * @date 2021/6/6 - 16:23
 */
public class Provider {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("logs_topic","topic");
        //发送消息
        String routerkey = "user.save.r";
        channel.basicPublish("logs_topic",routerkey,null,("这是基于routerkey["+routerkey+"]的消息发布,发送的消息为:好男人").getBytes());
        //关闭资源
        RabbitMqUtils.closeConnectionAndChanel(channel,connection);
    }
}
