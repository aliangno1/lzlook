package com.lzlook.backend.controller;

import com.alibaba.fastjson.JSON;
import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.dto.response.EntityResponse;
import com.lzlook.backend.dto.response.ListResponse;
import com.lzlook.backend.service.NovelServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("novel")
public class LzlookController {

    @Autowired
    private NovelServiceLocator novelServiceLocator;

    @RequestMapping("/search")
    public ListResponse<SearchResult> search(@RequestParam String keyword) {
        ListResponse<SearchResult> response = new ListResponse<>();
        Long start = System.currentTimeMillis();
        List<SearchResult> list = novelServiceLocator.search(keyword);
        Long end = System.currentTimeMillis();
        System.out.println("search cost time:" + (end - start) + "ms");
        System.out.println(JSON.toJSONString(list));
        response.success(list);
        return response;
    }

    @RequestMapping("/novel")
    public EntityResponse<Novel> novel(@RequestParam String url) {
        EntityResponse<Novel> response = new EntityResponse<>();
        Novel novel = novelServiceLocator.novel(url);
        response.success(novel);
        return response;
    }

    @RequestMapping("/chapter")
    public EntityResponse<Chapter> chapter(@RequestParam String url) {
        EntityResponse<Chapter> response = new EntityResponse<>();
        Chapter chapter = novelServiceLocator.chapter(url);
        response.success(chapter);
        return response;
    }

}
