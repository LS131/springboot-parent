package com.baizhi.fanout;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ls
 * @date 2021/6/6 - 20:09
 */
@Component
public class fanoutCustomer {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,// 创建临时队列
                    exchange = @Exchange(value = "logs",type = "fanout") //绑定的交换机
            )
    })
    public void receive1(String message){
        System.out.println("message1:" + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,// 创建临时队列
                    exchange = @Exchange(value = "logs",type = "fanout") //绑定的交换机
            )
    })
    public void receive12(String message){
        System.out.println("message2:" + message);
    }
}
