package com.lzlook.backend.util;

import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.service.NovelServiceLocator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.Future;

@Service
public class SearchUtil {

    private final NovelServiceLocator novelServiceLocator;

    public SearchUtil(NovelServiceLocator novelServiceLocator) {
        this.novelServiceLocator = novelServiceLocator;
    }

    @Async
    public Future<SearchResult> parseResultInfo(String url) {
        Document doc;
        SearchResult result;
        try {
//            Long startTime = System.currentTimeMillis();
            doc = Jsoup.connect(url).get();
//            Long endTime = System.currentTimeMillis();
//            System.out.println(url + " connect cost time:" + (endTime - startTime));
            String location = doc.location();
            String source = new URL(location).getHost();
            result = new SearchResult();
            result.setTitle(doc.title());
            result.setSource(source);
            result.setUrl(location);
            if (novelServiceLocator.getNovelCrawlerServiceMap().containsKey(source)) {
                result.setParsed(true);
            } else {
                result.setParsed(false);
            }
        } catch (Exception e) {
            return null;
        }
        return new AsyncResult<>(result);
    }
}
