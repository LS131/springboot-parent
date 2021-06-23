package com.baizhi.miaosha.entity;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author ls
 * @date 2021/6/5 - 8:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class Stock {
    private Integer id;
    private  String name;
    private Integer count;
    private Integer sale;
    private Integer version;


}
