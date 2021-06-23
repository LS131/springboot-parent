package com.baizhi.miaosha.dao;

import com.baizhi.miaosha.entity.Stock;

import java.util.Stack;

/**
 * @author ls
 * @date 2021/6/5 - 8:12
 */

public interface StockDao {
    //根据商品的id查询库存信息的方法
    Stock checkStock(int id);
    //扣除库存
    int updateSale(Stock stock);

}
