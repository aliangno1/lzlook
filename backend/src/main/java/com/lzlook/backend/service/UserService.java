package com.lzlook.backend.service;

import com.lzlook.backend.bean.User;

public interface UserService extends IService<User>{
    User findUserByName(String name);
}
