package com.lzlook.backend.service;

import com.alibaba.fastjson.JSON;
import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.util.FontUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
public class NovelServiceLocator implements ApplicationContextAware {

    private Map<String, NovelCrawlerService> novelCrawlerServiceMap;

    private Map<String, FetchEngineService> fetchEngineServiceMap;

    @Autowired
    private ValueOperations<String, Object> valueOperations;

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
        // 先从缓存中获取
        Object redisValue = valueOperations.get(String.valueOf(keyword.hashCode()));
        if (redisValue != null) {
            list = JSON.parseArray(FontUtil.decodeUnicode(String.valueOf(redisValue)), SearchResult.class);
            return sort(list);
        }
        // 小说网站站内搜索
        list.addAll(searchFromNovelSite(keyword));
        // 搜索引擎搜索
        list.addAll(searchFromEngine(keyword));

        // 对同一小说源去重
        List<SearchResult> results = new ArrayList<>();
        list.forEach(result -> {
            if (results.stream().noneMatch(r ->
                    r.getSource().equals(result.getSource())
            )) {
                results.add(result);
            } else {
                System.out.println("去重：" + result.getSource() + "  " + JSON.toJSON(result));
            }
        });
        // 将每次查询结果放入缓存，过期时间设置为10分钟
        valueOperations.set(String.valueOf(keyword.hashCode()), FontUtil.chinaToUnicode(JSON.toJSONString(list)), 10 * 60 * 1000, TimeUnit.MILLISECONDS);
        return sort(results);
    }

    private List<SearchResult> sort(List<SearchResult> list) {
        // 已解析网站靠前
        list.sort((o1, o2) -> {
            if (o1.getParsed() && o2.getParsed()) {
                return 0;
            } else if (o1.getParsed()) {
                return -1;
            } else {
                return 1;
            }
        });
        return list;
    }


    private Collection<? extends SearchResult> searchFromEngine(String keyword) {
        List<SearchResult> list = new ArrayList<>();

        List<Future<List<SearchResult>>> resultsFutureList = new ArrayList<>();
        for (FetchEngineService fetchEngine : fetchEngineServiceMap.values()) {
            Future<List<SearchResult>> resultsFuture = fetchEngine.search(keyword);
            resultsFutureList.add(resultsFuture);
        }

        for (Future<List<SearchResult>> resultsFeature : resultsFutureList) {
            try {
                List<SearchResult> results = resultsFeature.get(5, TimeUnit.SECONDS);
                if (!results.isEmpty()) {
                    list.addAll(results);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private Collection<? extends SearchResult> searchFromNovelSite(String keyword) {
        List<SearchResult> list = new ArrayList<>();

        List<Future<SearchResult>> resultFutureList = new ArrayList<>();
        for (NovelCrawlerService novelCrawler : novelCrawlerServiceMap.values()) {
            Future<SearchResult> resultFuture = novelCrawler.search(keyword);
            resultFutureList.add(resultFuture);
        }

        for (Future<SearchResult> resultFeature : resultFutureList) {
            try {
                SearchResult result = resultFeature.get(3, TimeUnit.SECONDS);
                if (result != null) {
                    list.add(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
