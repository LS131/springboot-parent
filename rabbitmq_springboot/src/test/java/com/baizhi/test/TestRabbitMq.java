package com.baizhi.test;

import com.baizhi.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ls
 * @date 2021/6/6 - 19:46
 */
@SpringBootTest(classes = DemoApplication.class)
@RunWith(SpringRunner.class)
public class TestRabbitMq {
    //注入rabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 值连
     */
    @Test
    public void testHello(){
        rabbitTemplate.convertAndSend("hello","hello world");
    }

    @Test
    public void testWork(){
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("work","work 模型"+String.valueOf(i));
        }
    }

    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("logs","","fanout模型发送的消息");
    }

    @Test
    public void testRoute(){
        rabbitTemplate.convertAndSend("directs","error","发送的info级别的信息");
    }

    @Test
    public void testTopic(){
        rabbitTemplate.convertAndSend("topics","order.save.9","user.save 路由信息");
    }


}
