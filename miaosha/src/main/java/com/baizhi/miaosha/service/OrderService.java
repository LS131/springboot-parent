package com.baizhi.miaosha.service;

/**
 * @author ls
 * @date 2021/6/5 - 8:08
 */
public interface OrderService {
    // 用来处理秒杀的下单方法 并返回订单的ID
    int kill(Integer id);
    // 生成随机码
    String getMd5(Integer id, Integer userid);

    // 用来处理秒杀的下单方法 并返回订单的ID 加入了Md5签名接口隐藏的方式
    int kill(Integer id, Integer userId, String md5);
}
