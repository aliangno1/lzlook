package com.lzlook.backend.service.impl.engine;

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

@Service("baidu")
public class BaiduFetchEngine implements FetchEngineService {

    private final static String searchUrl = "https://www.baidu.com/s?wd=";

//    @Autowired
//    private NovelServiceLocator novelServiceLocator;

    private final SearchUtil searchUtil;

    public BaiduFetchEngine(SearchUtil searchUtil) {
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
            doc = Jsoup.connect(searchUrl + keyword).header("Cookie", "BAIDUID=267BF4B85A98F58D5B3FBD3F6E420CF0:FG=1; BIDUPSID=267BF4B85A98F58D5B3FBD3F6E420CF0; PSTM=1557886858; H_WISE_SIDS=132694_126126_114553_132656_113879_128067_131676_132687_120213_132287_133017_132910_132718_133045_131247_122159_132440_130763_132394_132378_132326_132212_131517_132261_118894_118876_131402_118845_118822_118789_131651_131576_132840_131533_131530_133158_132604_129564_132590_132784_130120_131873_132771_131196_132565_133302_129656_133164_127025_132557_132542_131035_131905_128891_132293_132551_129835_133067_132553_129644_131424_132267_132944_110085_127969_131506_133154_123289_131755_133194_127416_131547_132725_132626_131545_128808; BD_UPN=12314753; BDUSS=WNSY0RkUUlpUXZrZHQtN2djay12enZ3NUN2MmVEYTlZem1pNWFWd3FTOHUyflZkRVFBQUFBJCQAAAAAAAAAAAEAAABYQQghscq34gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAC5Ozl0uTs5da; cflag=13%3A3; BD_HOME=1; H_PS_PSSID=1467_21105_29567_29699_29221; BDRCVFR[feWj1Vr5u3D]=I67x6TjHwwYf0; delPer=0; BD_CK_SAM=1; PSINO=2; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; COOKIE_SESSION=87747_4_8_7_18_17_0_0_7_7_2_0_0_0_7_0_1574143315_1571969182_1574240222%7C9%23777213_84_1571969166%7C9; ZD_ENTRY=google; H_PS_645EC=5ef2y2AsF%2B9CQ%2Bkhi9lArZgM67kckR2hU8rKuk1bNXCclJi7CjO7rvzjZaoD%2BpnIFtXV; BDSVRTM=27").get();
            if (doc != null) {
                Elements elements = doc.select("div #content_left > div");
                if (elements.size() > 0) {
                    for (Element element : elements) {
                        Elements nodes = element.select("h3 > a");
                        if (nodes.size() > 0) {
                            Element node = nodes.get(0);
                            String title = node.html();
                            if (title.contains("章节")) {
                                String jumpUrl = node.attr("href");
                                System.out.println(jumpUrl);
                                Future<SearchResult> result = searchUtil.parseResultInfo(jumpUrl);
                                futureList.add(result);
                            }
                        }
                    }
                } else {
                    System.out.println("百度查询结果为零");
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
//        Long endTime = System.currentTimeMillis();
//        System.out.println("endTime:" + endTime);
//        System.out.println("baidu cost time:" + (endTime - startTime));
        return list;
    }

}
