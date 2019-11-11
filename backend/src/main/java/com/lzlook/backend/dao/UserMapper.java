package com.lzlook.backend.dao;

import com.lzlook.backend.bean.User;
import com.lzlook.backend.util.MyMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MyMapper<User> {
}