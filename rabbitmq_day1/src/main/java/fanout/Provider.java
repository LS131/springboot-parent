package fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ls
 * @date 2021/6/6 - 15:49
 */
public class Provider {
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取链接对象
        Connection connection = RabbitMqUtils.getConnection();
        // 获取连接中的通道对象
        Channel channel = connection.createChannel();
        //通道去绑定对应的交换机
        // 参数1：交换机名称
        // 参数2：交换机类型  fanout 广播类型    不存在就创建
        channel.exchangeDeclare("logs","fanout");

        channel.basicPublish("logs","",null,"fanout type message".getBytes());

        //释放资源
        RabbitMqUtils.closeConnectionAndChanel(channel,connection);

    }
}
