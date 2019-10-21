package com.lzlook.backend.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class Chapter {
    private String id;

    private String name;

    private String content;

    private String url;

    private String previous;

    private String next;
}
