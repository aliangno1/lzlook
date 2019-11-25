package com.lzlook.backend.service.impl.engine;

import com.alibaba.fastjson.JSON;
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
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service("sougou")
public class SougouFetchEngine implements FetchEngineService {
    private final static String searchUrl = "https://www.sogou.com/web?query=";

    private final SearchUtil searchUtil;

    public SougouFetchEngine(SearchUtil searchUtil) {
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
            doc = Jsoup.connect(searchUrl + keyword).header("Cookie","SUV=003774F778F46DC55CEE2650B5A69908; CXID=5E707BC48BB5B1EB7B5F70F0BED6F689; SUID=235248DF4D238B0A5D0223A6000B3D2D; IPLOC=CN1100; ABTEST=7|1573800631|v17; browerV=3; osV=1; SNUID=93D46D6CB0B52897F8FCC63CB1A451FE; sst0=589; ld=9Zllllllll2NLTmxlllllVTbK9klllllTNCOwZllllwlllllpylll5@@@@@@@@@@; ad=@hxutlllll2NLTmjlllllVTbKullllllTNCOwZllllGlllllpq2sB@@@@@@@@@@@; sct=2").get();
            if (doc != null) {
                Elements elements = doc.select("#main > div > div.results > div");
                if (elements.size() > 0) {
                    for (Element element : elements) {
                        Elements nodes = element.select("h3 > a");
                        if (nodes.size() > 0) {
                            Element node = nodes.get(0);
                            String title = node.html();
                            if (title.contains("章节")) {
                                String jumpUrl = node.attr("href");
                                Future<SearchResult> result = searchUtil.parseResultInfo("https://www.sogou.com" + jumpUrl);
                                futureList.add(result);
                            }
                        }
                    }
                } else {
                    System.out.println("搜狗查询结果为零");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Future<SearchResult> resultFuture : futureList) {
            try {
                SearchResult result = resultFuture.get(4, TimeUnit.SECONDS);
                if (result != null) {
                    list.add(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        System.out.println("-------Sougou---------");
//        System.out.println(JSON.toJSONString(list));
//        System.out.println("-------Sougou---------");
//        Long endTime = System.currentTimeMillis();
//        System.out.println("endTime:" + endTime);
//        System.out.println("baidu cost time:" + (endTime - startTime));
        return list;
    }
}
