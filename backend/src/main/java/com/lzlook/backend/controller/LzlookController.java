package com.lzlook.backend.controller;

import com.alibaba.fastjson.JSON;
import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.dto.response.EntityResponse;
import com.lzlook.backend.dto.response.ListResponse;
import com.lzlook.backend.service.FetchEngineService;
import com.lzlook.backend.service.NovelServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("novel")
public class LzlookController {

    @Autowired
    private NovelServiceLocator novelServiceLocator;

    @Autowired
    private FetchEngineService fetchEngineService;

    @RequestMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        ListResponse<SearchResult> response = new ListResponse<>();
        List<SearchResult> list = fetchEngineService.search(keyword);
        System.out.println(JSON.toJSONString(list));
        response.success(list);
        model.addAttribute("results", list);
        return "search";
//        return response;
    }

    //    @ResponseBody
    @RequestMapping("/novel")
    public String novel(@RequestParam String url, Model model) {
        EntityResponse<Novel> response = new EntityResponse<>();
        Novel novel = novelServiceLocator.novel(url);
        response.success(novel);
        model.addAttribute("novel", novel);
        return "novel";
//        return response;
    }

    //    @ResponseBody
    @RequestMapping("/chapter")
    public String chapter(@RequestParam String url, Model model) {
        EntityResponse<Chapter> response = new EntityResponse<>();
        Chapter chapter = novelServiceLocator.chapter(url);
        response.success(chapter);
        model.addAttribute("chapter", chapter);
        return "chapter";
//        return response;
    }

}
