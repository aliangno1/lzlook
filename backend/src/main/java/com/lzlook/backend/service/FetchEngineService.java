package com.lzlook.backend.service;

import com.lzlook.backend.bean.SearchResult;

import java.util.List;

public interface FetchEngineService {

    List<SearchResult> search(String keyword);
}
