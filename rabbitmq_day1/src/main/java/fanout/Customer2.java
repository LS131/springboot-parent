package fanout;

import com.rabbitmq.client.*;
import utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ls
 * @date 2021/6/6 - 16:00
 */
public class Customer2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取链接对象
        Connection connection = RabbitMqUtils.getConnection();
        // 获取连接中的通道对象
        Channel channel = connection.createChannel();
        // 通道绑定交换机  参数1：交换机名 参数2：交换机类型 fanout为广播类型
        channel.exchangeDeclare("logs","fanout");
        // 临时队列
        String  queceName = channel.queueDeclare().getQueue();
        // 绑定交换机和队列
        // 1 队列名 2 交换机名 3 路由key
        channel.queueBind(queceName,"logs","");

        //消费数据
        channel.basicConsume(queceName,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者-2:"+new String(body));
            }
        });


    }
}
