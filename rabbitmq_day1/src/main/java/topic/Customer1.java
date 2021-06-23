package topic;

import com.rabbitmq.client.*;
import utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ls
 * @date 2021/6/6 - 16:33
 */
public class Customer1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare("logs_topic","topic");
        // 创建临时队列
        String quece = channel.queueDeclare().getQueue();
        // 基于routekey 绑定队列 与交换机
        channel.queueBind(quece,"logs_topic","user.*");

        //消费数据
        channel.basicConsume(quece,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者-1:"+new String(body));
            }
        });


    }
}
