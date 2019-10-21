package com.lzlook.backend.service.impl;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.service.NovelCrawlerService;
import org.springframework.stereotype.Service;

@Service("biqugeCrawlerService")
public class BiqugeCrawlerServiceImpl implements NovelCrawlerService {

    private final static String searchUrl = "";

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
        return null;
    }

    private Novel parseNovel(String url) {
        return null;
    }

    private Chapter parseChapter(String url) {
        return null;
    }
}
