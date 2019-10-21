package com.lzlook.backend.service.impl;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.service.NovelCrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service("dingdianCrawlerService")
public class DingdianCrawlerServiceImpl implements NovelCrawlerService {

    private final static String searchUrl = "https://www.x23us.com/modules/article/search.php?searchkey=";

    private final static String source = "www.x23us.com";

    @Override
    public SearchResult search(String keyword) {
        return parseSearchResult(keyword);
    }

    @Override
    public Novel novel(String url) {
        return parseNovel(url);
    }

    @Override
    public Chapter chapter(String url) {
        return parseChapter(url);
    }


    private SearchResult parseSearchResult(String keyword) {
        String encodedKeyword;
        Document doc = null;
        try {
            encodedKeyword = URLEncoder.encode(keyword, "GBK");
//            System.out.println(encodedKeyword);
            doc = Jsoup.connect(searchUrl + encodedKeyword).get();
        } catch (UnsupportedEncodingException e) {
            System.out.println("URLEncoder encode出错--dingdianCrawlerService");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("Jsoup解析出错--dingdianCrawlerService");
            e.printStackTrace();
        }
        SearchResult result = null;
        if (doc != null) {
            Element read = doc.select(".read").get(0);
            if (read != null) {
                result = new SearchResult();
                result.setUrl(read.attr("href"));
                result.setTitle(doc.title());
                result.setSource(source);
            }
        }
        return result;
    }

    private Novel parseNovel(String url) {
        return null;
    }

    private Chapter parseChapter(String url) {
        return null;
    }
}
