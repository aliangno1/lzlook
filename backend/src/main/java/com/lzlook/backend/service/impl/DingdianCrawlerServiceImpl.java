package com.lzlook.backend.service.impl;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.service.NovelCrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
        Document doc;
        SearchResult result = null;
        try {
            encodedKeyword = URLEncoder.encode(keyword, "GBK");
//            System.out.println(encodedKeyword);
            doc = Jsoup.connect(searchUrl + encodedKeyword).get();
            if (doc != null) {
                Element read = doc.select(".read").get(0);
                if (read != null) {
                    result = new SearchResult();
                    result.setUrl(read.attr("href"));
                    result.setTitle(doc.title());
                    result.setSource(source);
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("URLEncoder encode出错--dingdianCrawlerService");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("Jsoup解析出错--dingdianCrawlerService");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Novel parseNovel(String url) {
        Document doc;
        Novel novel = null;
        try {
            doc = Jsoup.connect(url).get();
            if (doc != null) {
                Elements chapterNodes = doc.select("#at > tbody > tr > td> a");
                if (chapterNodes != null) {
                    novel = new Novel();
                    novel.setSource(url);
                    List<Chapter> chapters = new ArrayList<>();
                    for (Element chapterNode : chapterNodes) {
                        Chapter chapter = new Chapter();
                        chapter.setUrl(url + chapterNode.attr("href"));
                        chapter.setName(chapterNode.html());
                        chapters.add(chapter);
                    }
                    novel.setChapters(chapters);
                }
            }
        } catch (IOException e) {
            System.out.println("Jsoup解析出错--dingdianCrawlerService");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return novel;
    }

    private Chapter parseChapter(String url) {
        return null;
    }
}
