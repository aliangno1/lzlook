package com.lzlook.backend.service;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;

import java.util.concurrent.Future;

public interface NovelCrawlerService {
    Future<SearchResult> search(String keyword);

    Novel novel(String url);

    Chapter chapter(String url);
}
