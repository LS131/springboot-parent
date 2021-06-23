package helloword;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;
import utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 直连模式点对点  （缺点：消息堆积  ）
 * @author ls
 * @date 2021/6/6 - 11:21
 */
public class Provider {
    // 生产消息
    @Test
    public void testSendMessage() throws IOException, TimeoutException {
        //获取链接对象
        Connection connection = RabbitMqUtils.getConnection();
        // 获取连接中的通道对象
        Channel channel = connection.createChannel();
        //通道去绑定对应的消息队列(队列的声明)
        // 参数1：队列名 不存在的话自动创建
        // 参数2：用来定义队列的特性 true 持久化队列（不丢失队列，但队列中消息会丢失） false 不持久化（systemctl restart rabbitmq-server 重启服务会丢失队列）
        // 参数3：exclusive 是否独占队列 true 独占队列（仅当前链接可用的队列） false 不独占队列
        // 参数4：autoDelete 是否载消费完成后自动删除队列（队列中的数据被消费完 消费者链接断开后） true 自动删除 false 不自动删除
        // 参数5：额外附加的参数
        // 注意:生产者与消费者的参数特性要严格与对应
        //注意: 相同名称的队列只能有一个
        channel.queueDeclare("hello",true,false,false,null);

        //basicPublish方法是真正发布消息的方法（并不是绑定了那个队列就只能向那个队列发消息）
        //参数1：交换机名称 参数2：队列名称 参数3：传递消息的额外设置 参数4：消息的具体内容
        //MessageProperties.PERSISTENT_TEXT_PLAIN 队列中消息做持久化 前提:队列一定做持久化（时机是在Mqs重启时将内存中需要持久化的数据持久化到硬盘，重启后再次读取）
        channel.basicPublish("","hello",MessageProperties.PERSISTENT_TEXT_PLAIN ,"hello rabbitmq".getBytes());

        RabbitMqUtils.closeConnectionAndChanel(channel,connection);



    }

}
