package workqueue;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ls
 * @date 2021/6/6 - 14:48
 */
public class Customer1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare("work",true,false,false,null);
        channel.basicQos(1);//每次只能消费一个消息
        //参数2：开启消息的自动确认机制 true 消费者自动向rabbitmq确认消息消费
        channel.basicConsume("work",false,new DefaultConsumer(channel){
            @SneakyThrows
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者-1:"+new String(body));
                Thread.sleep(3000);// 其实已经分配好的消息正在等待执行调用
                //手动确认 参数1：确认队列中那个具体的消息 参数2：是否开启多个消息同时确认 false 每次确认一个  确认后队列中的才会删除
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
    }
}
