package com.lzlook.backend.controller;

import com.alibaba.fastjson.JSON;
import com.lzlook.backend.bean.User;
import com.lzlook.backend.dto.response.EntityResponse;
import com.lzlook.backend.dto.response.Response;
import com.lzlook.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public EntityResponse<User> login(@RequestParam String name, @RequestParam String password, HttpServletRequest request) {
        EntityResponse<User> response = new EntityResponse<>();
        User user;
        try{
            user = userService.findUserByName(name);
        }catch (Exception e){
            e.printStackTrace();
            response.setCode(Response.FAILED);
            response.setMsg("服务器罢工了，请稍后访问。");
            return response;
        }
        if (user == null) {
            response.setCode(Response.FAILED);
            response.setMsg("用户不存在！");
        } else {
            if (user.getPassword().equals(password)) {
                response.success(user);
                HttpSession session = request.getSession();
                session.setAttribute("user", JSON.toJSONString(user));
            } else {
                response.setCode(Response.FAILED);
                response.setMsg("账号或密码错误！");
            }
        }
        return response;
    }

}
