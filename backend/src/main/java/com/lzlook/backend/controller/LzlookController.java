package com.lzlook.backend.controller;

import com.alibaba.fastjson.JSON;
import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.dto.response.EntityResponse;
import com.lzlook.backend.dto.response.ListResponse;
import com.lzlook.backend.service.NovelCrawlerService;
import com.lzlook.backend.service.NovelServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        List<SearchResult> list = novelServiceLocator.search(keyword);
        System.out.println(JSON.toJSONString(list));
        response.setList(list);
        return response;
    }

    @RequestMapping("/novel")
    public EntityResponse<Novel> novel(@RequestParam String url){
        EntityResponse<Novel> response = new EntityResponse<>();
        response.setEntity(novelServiceLocator.novel(url));
        return response;
    }

    @RequestMapping("/chapter")
    public EntityResponse<Chapter> chapter(@RequestParam String url){
        EntityResponse<Chapter> response = new EntityResponse<>();
        response.setEntity(novelServiceLocator.chapter(url));
        return response;
    }

}
