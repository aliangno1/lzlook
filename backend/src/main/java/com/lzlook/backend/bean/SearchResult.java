package com.lzlook.backend.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class SearchResult {
    private String id;

    private String source;

    private String title;

    private String url;

    private Boolean parsed;

    private String updateTime;

    private String LatestChapter;

}
