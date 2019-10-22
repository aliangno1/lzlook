package com.lzlook.backend.service.impl;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.constant.SearchType;
import com.lzlook.backend.service.FetchEngineService;
import com.lzlook.backend.service.NovelCrawlerService;
import com.lzlook.backend.service.NovelServiceLocator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service("baidu")
public class BaiduCrawlerServiceImpl implements FetchEngineService {

    private final static String searchUrl = "https://www.baidu.com/s?wd=";

    @Autowired
    private NovelServiceLocator novelServiceLocator;

    @Override
    public List<SearchResult> search(String keyword) {
        return parseSearchResult(keyword);
    }

    private List<SearchResult> parseSearchResult(String keyword) {
        Document doc;
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
                                SearchResult result = novelServiceLocator.parseResultInfo(jumpUrl);
                                if (result != null) {
                                    list.add(result);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
