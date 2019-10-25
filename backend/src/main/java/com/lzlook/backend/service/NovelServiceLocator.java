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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class NovelServiceLocator implements ApplicationContextAware {

    private Map<String, NovelCrawlerService> novelCrawlerServiceMap;

    private Map<String, FetchEngineService> fetchEngineServiceMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        novelCrawlerServiceMap = applicationContext.getBeansOfType(NovelCrawlerService.class);
        fetchEngineServiceMap = applicationContext.getBeansOfType(FetchEngineService.class);
    }

    public Map<String, NovelCrawlerService> getNovelCrawlerServiceMap() {
        return this.novelCrawlerServiceMap;
    }

    public List<SearchResult> search(String keyword) {
        List<SearchResult> list = new ArrayList<>();
        List<Future<SearchResult>> resultFutureList = new ArrayList<>();
        for (NovelCrawlerService novelCrawler : novelCrawlerServiceMap.values()) {
            Future<SearchResult> resultFuture = novelCrawler.search(keyword);
            resultFutureList.add(resultFuture);
        }

        List<Future<List<SearchResult>>> resultsFutureList = new ArrayList<>();
        for (FetchEngineService fetchEngine : fetchEngineServiceMap.values()) {
            Future<List<SearchResult>> resultsFuture = fetchEngine.search(keyword);
            resultsFutureList.add(resultsFuture);
        }

        for (Future<SearchResult> resultFeature : resultFutureList) {
            try {
                SearchResult result = resultFeature.get(10,TimeUnit.SECONDS);
                if (result != null) {
                    list.add(result);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }

        for (Future<List<SearchResult>> resultsFeature : resultsFutureList) {
            try {
                List<SearchResult> results = resultsFeature.get(10,TimeUnit.SECONDS);
                if (!results.isEmpty()) {
                    list.addAll(results);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }
        // todo:同一小说源去重，推荐源 放前面
        return list;
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
