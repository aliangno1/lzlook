package com.lzlook.backend.service;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NovelServiceLocator implements ApplicationContextAware {

    private Map<String, NovelCrawlerService> novelCrawlerServiceMap;

    private static Map<String, String> novelSiteMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        novelCrawlerServiceMap = applicationContext.getBeansOfType(NovelCrawlerService.class);
        novelSiteMap.put("www.x23us.com", "dingdian");
        novelSiteMap.put("www.biquge.info", "biquge");
    }

    public List<SearchResult> search(String keyword) {
        List<SearchResult> results = new ArrayList<>();
        for (NovelCrawlerService novelCrawler : novelCrawlerServiceMap.values()) {
            SearchResult result = novelCrawler.search(keyword);
            if(result != null){
                results.add(result);
            }
        }
        return results;
    }

    public Novel novel(String url) {
        NovelCrawlerService novelCrawlerService = parse(url);
        return novelCrawlerService == null ? null : novelCrawlerService.novel(url);
    }

    public Chapter chapter(String url) {
        NovelCrawlerService novelCrawlerService = parse(url);
        return novelCrawlerService == null ? null : novelCrawlerService.chapter(url);
    }

    private NovelCrawlerService parse(String url) {
        String site;
        try {
            site = novelSiteMap.get(new URL(url).getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return site == null ? null :novelCrawlerServiceMap.get(site);
    }


}
