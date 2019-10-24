package com.lzlook.backend.service.impl;

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


@Service("www.x23us.com")
public class DingdianCrawlerServiceImpl implements NovelCrawlerService {

    private final static String searchUrl = "https://www.x23us.com/modules/article/search.php?searchkey=";

    private final static String source = "www.x23us.com";

    private final static String sourceUrl = "https://www.x23us.com";

    @Override
    @Async
    public Future<SearchResult> search(String keyword) {
//        Long start = System.currentTimeMillis();
        SearchResult result = parseSearchResult(keyword, SearchType.KEYWORD);
//        Long end = System.currentTimeMillis();
//        System.out.println(source + " cost time: " + (end - start));
        return new AsyncResult<>(result);
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
        String encodedKeyword;
        Document doc;
        SearchResult result = null;
        try {
            if (type == SearchType.URL) {
                doc = Jsoup.connect(keyword).get();
            } else {
                encodedKeyword = URLEncoder.encode(keyword, "GBK");
                doc = Jsoup.connect(searchUrl + encodedKeyword).get();
            }
            if (doc != null) {
                Elements reads = doc.select(".read");
                Element read = reads.size() < 1 ? null : reads.get(0);
                if (read != null) {
                    result = new SearchResult();
                    result.setUrl(read.attr("href"));
                    result.setTitle(doc.title());
                    result.setSource(source);
                } else {
                    Elements books = doc.select("#content > table > tbody > tr > td:nth-child(1) > a");
                    for (Element book : books) {
                        if (book.childNodeSize() == 1) {
                            result = parseSearchResult(book.attr("href"), SearchType.URL);
                        }
                    }
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
        Document doc;
        Chapter chapter = null;
        try {
            doc = Jsoup.connect(url).get();
            if (doc != null) {
                Element name = doc.select("#amain > dl > dd:nth-child(2) > h1").get(0);
                Element contents = doc.select("#contents").get(0);
                Element previous = doc.select("#amain > dl > dd:nth-child(3) > h3 > a:nth-child(1)").get(0);
                Element next = doc.select("#amain > dl > dd:nth-child(3) > h3 > a:nth-child(3)").get(0);
                chapter = new Chapter();
                chapter.setUrl(url);
                chapter.setName(name.html());
                chapter.setContent(contents.html().replaceAll("\n", ""));
                chapter.setPrevious(sourceUrl + previous.attr("href"));
                chapter.setNext(sourceUrl + next.attr("href"));
            }
        } catch (IOException e) {
            System.out.println("Jsoup解析出错--dingdianCrawlerService");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapter;
    }
}
