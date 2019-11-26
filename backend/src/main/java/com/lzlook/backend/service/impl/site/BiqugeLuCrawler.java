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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Service("www.biquge.lu")
public class BiqugeLuCrawler implements NovelCrawlerService {
    private final static String source = "www.biquge.lu";
    private final static String sourceUri = "http://www.biquge.lu";
    private final static String searchUrl = "https://so.biqusoso.com/s.php?ie=utf-8&siteid=biquge.lu&q=";

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
        Document doc;
        SearchResult result = null;
        try {
            doc = Jsoup.connect(searchUrl + keyword).get();
            if (doc != null) {
                Elements infos = doc.select("#search-main > div.search-list > ul > li:nth-child(2) > span.s2 > a");
                Element info = infos.size() < 1 ? null : infos.get(0);
                if (info != null) {
                    String url = info.attr("href");
                    doc = Jsoup.connect(url).get();
                    result = new SearchResult();
                    result.setUrl(doc.location());
                    result.setTitle(doc.title());
                    result.setSource(source);
                    result.setParsed(true);
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("URLEncoder encode出错--" + source);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("Jsoup解析出错--" + source);
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
            novel = new Novel();
            novel.setName(doc.select("body > div.book > div.info > h2").get(0).html());
            novel.setSource(url);
            Elements chapterNodes = doc.select("body > div.listmain > dl > dd > a");
            List<Chapter> chapters = new ArrayList<>();
            for (Element node : chapterNodes) {
                Chapter chapter = new Chapter();
                chapter.setUrl(sourceUri + node.attr("href"));
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

            chapter.setName(doc.select("#wrapper > div.book.reader > div.content > h1").get(0).html());
            chapter.setContent(doc.select("#content").html().replaceAll("\n", ""));
            String previousUrl = doc.select("#wrapper > div.book.reader > div.content > div.page_chapter > ul > li:nth-child(1) > a").get(0).attr("href");
            String nextUrl = doc.select("#wrapper > div.book.reader > div.content > div.page_chapter > ul > li:nth-child(3) > a").get(0).attr("href");
            previousUrl = previousUrl.endsWith(".html") ? sourceUri + previousUrl : url;
            nextUrl = nextUrl.endsWith(".html") ? sourceUri + nextUrl : url;
            chapter.setPrevious(previousUrl);
            chapter.setNext(nextUrl);
        } catch (Exception e) {
            System.out.println("Jsoup解析出错--" + source);
            e.printStackTrace();
        }
        return chapter;
    }
}
