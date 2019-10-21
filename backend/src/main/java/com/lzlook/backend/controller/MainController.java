package com.lzlook.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("lzlook")
public class MainController {

    @RequestMapping("")
    public String home(){
        return "lzlook";
    }
}
