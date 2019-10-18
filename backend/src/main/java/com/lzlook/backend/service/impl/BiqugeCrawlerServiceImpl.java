package com.lzlook.backend.service.impl;

import com.lzlook.backend.bean.Chapter;
import com.lzlook.backend.bean.Novel;
import com.lzlook.backend.bean.SearchResult;
import com.lzlook.backend.service.NovelCrawlerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("biqugeCrawlerService")
public class BiqugeCrawlerServiceImpl implements NovelCrawlerService {

    @Override
    public List<SearchResult> search(String keyword) {
        List<SearchResult> list = new ArrayList<>();
        SearchResult searchResult = new SearchResult();
        searchResult.setTitle("成功使用笔趣阁了么？");
        list.add(searchResult);
        return list;
    }

    @Override
    public Novel novel(String url) {
        Novel novel = new Novel();
        novel.setName("变成血族是什么体验？");
        return novel;
    }

    @Override
    public Chapter chapter(String url) {
        Chapter chapter = new Chapter();
        chapter.setName("拆礼物");
        return chapter;
    }
}
