package com.lzlook.backend.service;

import com.lzlook.backend.bean.SearchResult;

import java.util.List;
import java.util.concurrent.Future;

public interface FetchEngineService {
    Future<List<SearchResult>> search(String keyword);
}
