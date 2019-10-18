package com.lzlook.backend.controller;

import com.alibaba.fastjson.JSON;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.dto.response.ListResponse;
import com.lzlook.backend.service.NovelCrawlerService;
import com.lzlook.backend.service.impl.BiqugeCrawlerServiceImpl;
import com.lzlook.backend.service.impl.DingdianCrawlerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("novel")
public class LzlookController {

    @Autowired
    @Qualifier("biqugeCrawlerService")
    private NovelCrawlerService novelCrawlerService;

    @RequestMapping("/search")
    public ListResponse<SearchResult> search(@RequestParam String keyword) {
        ListResponse<SearchResult> response = new ListResponse<>();
        getCrawlerService(keyword);
        List<SearchResult> list = novelCrawlerService.search(keyword);
        System.out.println(JSON.toJSONString(list));
        response.setList(list);
        return response;
    }

    private void getCrawlerService(String keyword) {
        if("dingdian".equals(keyword)){
            novelCrawlerService = new DingdianCrawlerServiceImpl();
        }else if ("biquge".equals(keyword)){
            novelCrawlerService = new BiqugeCrawlerServiceImpl();
        }
    }
}
