package com.lzlook.backend.bean;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private String id;

    private String source;

    private String title;

    private String url;

    private String updateTime;

    private String LatestChapter;

    private List<Chapter> chapters;
}
