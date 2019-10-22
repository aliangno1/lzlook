package com.lzlook.backend.service;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class NovelServiceLocator implements ApplicationContextAware {

    private Map<String, NovelCrawlerService> novelCrawlerServiceMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        novelCrawlerServiceMap = applicationContext.getBeansOfType(NovelCrawlerService.class);
    }

    public SearchResult parseResultInfo(String url) {
        Document doc;
        SearchResult result;
        try {
            doc = Jsoup.connect(url).get();
            String location = doc.location();
            String source = new URL(location).getHost();
            result = new SearchResult();
            result.setTitle(doc.title());
            result.setSource(source);
            result.setUrl(location);
            if (novelCrawlerServiceMap.containsKey(source)) {
                result.setParsed(true);
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public List<SearchResult> search(String keyword) {
        List<SearchResult> results = new ArrayList<>();
        for (NovelCrawlerService novelCrawler : novelCrawlerServiceMap.values()) {
            SearchResult result = novelCrawler.search(keyword);
            if (result != null) {
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
            site = new URL(url).getHost();
            return novelCrawlerServiceMap.get(site);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }


}
