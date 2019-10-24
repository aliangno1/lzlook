package com.lzlook.backend.service.impl;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.constant.SearchType;
import com.lzlook.backend.service.NovelCrawlerService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Future;

//@Service("www.biquge.info")
public class BiqugeCrawlerServiceImpl implements NovelCrawlerService {

    private final static String searchUrl = "";

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
        return null;
    }

    private Novel parseNovel(String url) {
        return null;
    }

    private Chapter parseChapter(String url) {
        return null;
    }
}
