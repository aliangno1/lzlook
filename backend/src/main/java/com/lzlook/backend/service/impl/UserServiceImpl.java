package com.lzlook.backend.service.impl;

import com.lzlook.backend.bean.User;
import com.lzlook.backend.dao.UserMapper;
import com.lzlook.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserByName(String name) {
        return userMapper.selectByUserName(name);
    }
}
