package com.baizhi.miaosha.dao;

import com.baizhi.miaosha.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    User findById(Integer id);
}