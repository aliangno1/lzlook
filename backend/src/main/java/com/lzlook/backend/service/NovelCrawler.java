package com.lzlook.backend.service;

import java.util.List;

public interface NovelCrawler {
    Object search(String keyword);

    List<String> chapter(String url);
}
