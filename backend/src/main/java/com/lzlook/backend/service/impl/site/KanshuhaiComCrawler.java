package com.lzlook.backend.service.impl.site;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.constant.SearchType;
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

@Service("www.kanshuhai.com")
public class KanshuhaiComCrawler implements NovelCrawlerService {
    private final static String source = "www.kanshuhai.com";
    private final static String sourceUrl = "https://www.kanshuhai.com";
    private final static String searchUrl = "https://www.kanshuhai.com/modules/article/search.php";

    @Override
    @Async
    public Future<SearchResult> search(String keyword) {
        return new AsyncResult<>(parseSearchResult(keyword, SearchType.KEYWORD));
    }

    @Override
    public Novel novel(String url) {
        return parseNovel(url);
    }

    @Override
    public Chapter chapter(String url) {
        return parseChapter(url);
    }

    private SearchResult parseSearchResult(String keyword, SearchType type) {
//        String encodedKeyword;
//        Document doc;
//        SearchResult result = null;
//        try {
//            if (type == SearchType.URL) {
//                doc = Jsoup.connect(keyword).get();
//            } else {
//                encodedKeyword = URLEncoder.encode(keyword, "GBK");
//                doc = Jsoup.connect(searchUrl).data("searchkey", encodedKeyword, "searchtype", "articlename").post();
//            }
//            if (doc != null) {
//                Elements infos = doc.select("#bookshow > div > div.details > div.book-info > div.book-link > a.orange");
//                Element info = infos.size() < 1 ? null : infos.get(0);
//                if (doc != null) {
//                    doc = Jsoup.connect(info.attr("href")).get();
//                    result = new SearchResult();
//                    result.setUrl(doc.location());
//                    result.setTitle(doc.title());
//                    result.setSource(source);
//                    result.setParsed(true);
//                } else {
//                    Elements books = doc.select("#content > table > tbody > tr > td:nth-child(1) > a");
//                    for (Element book : books) {
//                        if (keyword.equals(book.html())) {
//                            result = parseSearchResult(sourceUrl + book.attr("href"), SearchType.URL);
//                        }
//                    }
//                }
//
//            }
//        } catch (UnsupportedEncodingException e) {
//            System.out.println("URLEncoder encode出错--" + source);
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            System.out.println("Jsoup解析出错--" + source);
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
        return null;
    }

    private Novel parseNovel(String url) {
        Document doc;
        Novel novel = null;
        try {
            doc = Jsoup.connect(url).get();
            novel = new Novel();
            novel.setName(doc.select("#title > h1").get(0).html());
            novel.setSource(url);
            Elements chapterNodes = doc.select("#book > dl > dd > a");
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
            chapter.setName(doc.select("#title > h1").get(0).html());
            chapter.setContent(doc.select("#con_left").html().replaceAll("\n", ""));
            String previousUrl = doc.select("#link > a:nth-child(2)").get(0).attr("href");
            String nextUrl = doc.select("#link > a:nth-child(4)").get(0).attr("href");
            previousUrl = previousUrl.endsWith("html") ? previousUrl : url;
            nextUrl = nextUrl.endsWith("html") ? nextUrl : url;
            chapter.setPrevious(previousUrl);
            chapter.setNext(nextUrl);
        } catch (IOException e) {
            System.out.println("Jsoup解析出错--" + source);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapter;
    }
}
