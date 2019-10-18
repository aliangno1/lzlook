package com.lzlook.backend.bean;

import lombok.Data;

@Data
public class Chapter {
    private String id;

    private String name;

    private String content;

    private String url;

    private String previous;

    private String next;
}
