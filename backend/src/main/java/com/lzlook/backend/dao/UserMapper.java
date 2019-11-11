package com.lzlook.backend.dao;

import com.lzlook.backend.bean.User;
import com.lzlook.backend.util.MyMapper;

public interface UserMapper extends MyMapper<User> {
    User selectByUserName(String name);
}