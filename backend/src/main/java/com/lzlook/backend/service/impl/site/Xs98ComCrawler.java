package com.lzlook.backend.service.impl.site;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.service.NovelCrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Service("www.xs98.com")
public class Xs98ComCrawler implements NovelCrawlerService {
    private final static String source = "www.xs98.com";
    private final static String sourceUri = "https://www.xs98.com";
    private final static String searchUrl = "http://www.xbiquge.la/modules/article/search.php";

    @Override
    @Async
    public Future<SearchResult> search(String keyword) {
        return new AsyncResult<>(parseSearchResult(keyword));
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
        return null;
    }

    private Novel parseNovel(String url) {
        Document doc;
        Novel novel = null;
        try {
            doc = Jsoup.connect(url).get();
            novel = new Novel();
            novel.setName(doc.select("#info > h1").get(0).html());
            novel.setSource(url);
            Elements chapterNodes = doc.select("#list > dl > dd > a");
            List<Chapter> chapters = new ArrayList<>();
            for (Element node : chapterNodes) {
                Chapter chapter = new Chapter();
                chapter.setUrl(url + node.attr("href"));
                chapter.setName(node.html());
                chapters.add(chapter);
            }
            novel.setChapters(chapters);
        } catch (IOException e) {
            System.out.println("Jsoup解析出错--" + source);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return novel;
    }

    private Chapter parseChapter(String url) {
        Document doc;
        Chapter chapter = null;
        try {
            doc = Jsoup.connect(url).get();
            chapter = new Chapter();
            chapter.setUrl(url);

            chapter.setName(doc.select("#wrapper > div.content_read > div > div.bookname > h1").get(0).html());
            chapter.setContent(doc.select("#content").html().replaceAll("\n", ""));
            String previousUrl = doc.select("#pager_prev").get(0).attr("href");
            String internalUrl = doc.select("#pager_current").get(0).attr("href");
            String nextUrl = doc.select("#pager_next").get(0).attr("href");
            previousUrl = previousUrl.endsWith(".html") ? sourceUri + internalUrl + previousUrl : url;
            nextUrl = nextUrl.endsWith(".html") ? sourceUri + internalUrl + nextUrl : url;
            chapter.setPrevious(previousUrl);
            chapter.setNext(nextUrl);
        } catch (Exception e) {
            System.out.println("Jsoup解析出错--" + source);
            e.printStackTrace();
        }
        return chapter;
    }
}
