package com.lzlook.backend.util;

import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.service.NovelServiceLocator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
            doc = Jsoup.connect(url).timeout(2000).get();
            if ("www.sogou.com".equals(new URL(doc.location()).getHost())) {
                // 搜狗引擎特殊处理
                Elements meta = doc.select("html head meta");
                Element target = meta.get(1);
                if (target.attr("http-equiv").contains("refresh")) {
                    List<String> temp = Arrays.asList(target.attr("content").split("="));
                    url = temp.get(1).substring(1, temp.get(1).length() - 1);
                } else {
                    if (doc.toString().contains("window.location.replace")) {
                        meta = doc.select("script");
                        for (Element script : meta) {
                            String s = script.data();
                            if (!s.isEmpty() && s.startsWith("window.location.replace")) {
                                int start = s.indexOf("=");
                                int end = s.indexOf(";");
                                if (start > 0 && end > start) {
                                    s = s.substring(start + 1, end);
                                    s = s.replace("'", "").replace("\"", "");
                                    url = s.trim();
                                    break;
                                }
                            }
                        }
                    }
                }
                doc = Jsoup.connect(url).timeout(2000).get();
            }
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
            System.out.println(url + " connect timeout.");
            e.printStackTrace();
            return null;
        }
        return new AsyncResult<>(result);
    }
}
