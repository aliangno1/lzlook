package com.lzlook.backend.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class Novel {
    private String id;

    private String name;

    private String author;

    private String source;

    private List<Chapter> chapters;
}
