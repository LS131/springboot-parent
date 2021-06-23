package com.baizhi.miaosha.entity;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author ls
 * @date 2021/6/5 - 8:31
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@ToString
public class Order {
    private Integer id;
    private Integer sid;
    private String name;
    private Date createDate;

}
