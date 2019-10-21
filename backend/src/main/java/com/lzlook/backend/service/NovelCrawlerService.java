package com.lzlook.backend.service;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;

public interface NovelCrawlerService {
    SearchResult search(String keyword);

    Novel novel(String url);

    Chapter chapter(String url);
}