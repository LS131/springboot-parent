package com.baizhi.miaosha.controller;

import com.baizhi.miaosha.service.OrderService;
import com.baizhi.miaosha.service.UserService;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author ls
 * @date 2021/6/5 - 7:59
 */
@Slf4j
@Api(tags = "秒杀系统接口规范说明")
@RestController
@RequestMapping("stock")
public class StockController {

    // 创建令牌桶的实例  每秒十个
    private RateLimiter rateLimiter = RateLimiter.create(40);
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    //开发秒杀方法
    @GetMapping("kill")
    @ApiOperation(value = "秒杀接口",notes = "<span style='color:red;'>描述:</span>&nbsp;执行秒杀动作的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "商品编号",dataType = "Integer",defaultValue = "0")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    public String kill(Integer id){
        System.out.println("秒杀商品的ID:"+id);
        try{
            //根据商品的id去调用秒杀
            int orderId = orderService.kill(id);
            return "秒杀成功,订单的ID为:"+String.valueOf(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
    // 乐观锁 + 令牌桶限流 + Redis限时
    @GetMapping("killToken")
    public String killToken(Integer id){
        // 1 没有获取到token的请求一直阻塞 直到获取到token令牌
        //log.info("等待的时间:"+rateLimiter.acquire());
        // 2 设置一个等待时间 如果载等待的时间内获取到token令牌 则返回true 处理业务逻辑 如果载等待的时间内没有获取到响应token则抛弃
        if(!rateLimiter.tryAcquire(2, TimeUnit.SECONDS)) {
            System.out.println("当前请求被限流,直接抛弃无法调用后续的秒杀逻辑.....");
            return "抢购失败";
        }
        System.out.println("秒杀商品的ID:"+id);
        try{
            //根据商品的id去调用秒杀
            int orderId = orderService.kill(id);
            return "秒杀成功,订单的ID为:"+String.valueOf(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //生成md5值的方法
    @RequestMapping("md5")
    public String getMd5(Integer id, Integer userid) {
        String md5;
        try {
            md5 = orderService.getMd5(id, userid);
        }catch (Exception e){
            e.printStackTrace();
            return "获取md5失败: "+e.getMessage();
        }
        return "获取md5信息为: "+md5;
    }

    // 乐观锁 + 令牌桶限流 + Redis限时 + MD5
    @GetMapping("killTokenMd5")
    public String killTokenMd5(Integer id,Integer userId,String md5){
        if(!rateLimiter.tryAcquire(2, TimeUnit.SECONDS)) {
            System.out.println("当前请求被限流,直接抛弃无法调用后续的秒杀逻辑.....");
            return "抢购失败";
        }
        System.out.println("秒杀商品的ID:"+id);
        try{
            //根据商品的id去调用秒杀
            int orderId = orderService.kill(id,userId,md5);
            return "秒杀成功,订单的ID为:"+String.valueOf(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // 乐观锁 + 令牌桶限流 + Redis限时 + MD5 + 单用户访问频率限制
    @GetMapping("killTokenMd5Limit")
    public String killTokenLimit(Integer id,Integer userId,String md5){
        if(!rateLimiter.tryAcquire(2, TimeUnit.SECONDS)) {
            System.out.println("当前请求被限流,直接抛弃无法调用后续的秒杀逻辑.....");
            return "抢购失败";
        }
        try{
            //加入单用户限制调用频率
            int count = userService.saveUserCount(userId);
            log.info("用户截至该次的访问次数为: [{}]", count);
            boolean isBanned = userService.getUserCount(userId);
            if (isBanned) {
                log.info("购买失败,超过频率限制!");
                return "购买失败，超过频率限制!";
            }
            //根据商品的id去调用秒杀
            int orderId = orderService.kill(id,userId,md5);
            return "秒杀成功,订单的ID为:"+String.valueOf(orderId);
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
