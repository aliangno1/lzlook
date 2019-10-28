package com.lzlook.backend.service;

public interface SeedFetchService {
    String getItemInfo(String name);

    void exportData(String key1, String key2);
}
