package com.baizhi.hello;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ls
 * @date 2021/6/6 - 19:52
 */
@Component  //默认的创建的队列是持久化  非独占的  不是自动删除的
@RabbitListener(queuesToDeclare = @Queue(value = "hello",durable = "false",autoDelete = "true"))
public class helloCustomer {
    @RabbitHandler
    public void receivel(String message){
        System.out.println("message:"+message);
    }
}
