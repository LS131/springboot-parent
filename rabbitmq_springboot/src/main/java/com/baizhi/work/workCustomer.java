package com.baizhi.work;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ls
 * @date 2021/6/6 - 20:02
 */
@Component
public class workCustomer {
    /**
     * 创建一个消费者
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void receivel1(String message){
        System.out.println("message1="+message);
    }

    /**
     * 创建一个消费者
     * @param message
     */
    @RabbitListener(queuesToDeclare = @Queue("work"))
    public void receivel2(String message){
        System.out.println("message2="+message);
    }

}
