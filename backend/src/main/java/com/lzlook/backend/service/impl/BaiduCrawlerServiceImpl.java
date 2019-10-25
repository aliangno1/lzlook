package com.lzlook.backend.service.impl;

import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.service.FetchEngineService;
import com.lzlook.backend.util.SearchUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service("baidu")
public class BaiduCrawlerServiceImpl implements FetchEngineService {

    private final static String searchUrl = "https://www.baidu.com/s?wd=";

//    @Autowired
//    private NovelServiceLocator novelServiceLocator;

    private final SearchUtil searchUtil;

    public BaiduCrawlerServiceImpl(SearchUtil searchUtil) {
        this.searchUtil = searchUtil;
    }

    @Override
    @Async
    public Future<List<SearchResult>> search(String keyword) {
        return new AsyncResult<>(parseSearchResult(keyword));
    }

    private List<SearchResult> parseSearchResult(String keyword) {
        Document doc;
        List<Future<SearchResult>> futureList = new ArrayList<>();
//        Long startTime = System.currentTimeMillis();
//        System.out.println("startTime" + startTime);
        List<SearchResult> list = new ArrayList<>();
        try {
            doc = Jsoup.connect(searchUrl + keyword).get();
            if (doc != null) {
                Elements elements = doc.select("div #content_left > div");
                if (elements.size() > 0) {
                    for (Element element : elements) {
                        Elements nodes = element.select("h3 > a");
                        if (nodes.size() > 0) {
                            Element node = nodes.get(0);
                            String title = node.html();
                            if (title.contains("最新章节")) {
                                String jumpUrl = node.attr("href");
                                Future<SearchResult> result = searchUtil.parseResultInfo(jumpUrl);
                                futureList.add(result);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Future<SearchResult> resultFuture : futureList) {
            try {
                SearchResult result = resultFuture.get(10,TimeUnit.SECONDS);
                if (result != null) {
                    list.add(result);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }
//        Long endTime = System.currentTimeMillis();
//        System.out.println("endTime:" + endTime);
//        System.out.println("baidu cost time:" + (endTime - startTime));
        return list;
    }

}
