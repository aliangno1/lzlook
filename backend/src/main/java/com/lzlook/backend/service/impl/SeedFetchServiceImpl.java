package com.lzlook.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.lzlook.backend.service.SeedFetchService;
import com.lzlook.backend.util.FontUtil;
import com.lzlook.backend.util.JSONToExcel;
import com.lzlook.backend.util.SeedSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service("seed")
public class SeedFetchServiceImpl implements SeedFetchService {

    private RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    private static final String url = "http://www.cgris.net/query/o.php";

    @Override
    public String getItemInfo(String id) {
        String itemInfo;
        try {
            itemInfo = seedSearchUtil.getItemInfo(id, "粮食作物", "小麦").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
        return itemInfo;
    }

    @Autowired
    private SeedSearchUtil seedSearchUtil;

    private void println(String string) {
        System.out.println(new Date(System.currentTimeMillis()).toLocaleString() + "  " + string);
    }

    @Override
    public void exportData(String key1, String key2) {
        Long start = System.currentTimeMillis();
        List<String> itemIdList;
        Scanner in = null;
        StringBuilder idListString = new StringBuilder();
        println("正在进行文件解析...");
        try {
            in = new Scanner(new File("D:\\ids.txt"));
            while (in.hasNext()) {
                idListString.append(in.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            println("ids.txt文件解析失败！");
            return;
        }

        itemIdList = Arrays.asList(idListString.toString().replaceAll("\"", "").replaceAll("\\[", "").replaceAll("]", "").split(","));
        println("itemIdList: " + JSON.toJSON(itemIdList));

        if (itemIdList.isEmpty()) {
            println("id列表为空！");
            return;
        }

        Map<String, Future<String>> futureMap = new HashMap<>();
//        List<Future<String>> futureList = new ArrayList<>();
        for (String id : itemIdList) {
            Future<String> itemFuture = seedSearchUtil.getItemInfo(id, key1, key2);
            futureMap.put(id, itemFuture);
//            futureList.add(itemFuture);
        }
        println("开始数据爬取...");

        List<String> itemList = new ArrayList<>();
        List<String> failedIdList = new ArrayList<>();
        for (Map.Entry<String, Future<String>> itemFutureEntry : futureMap.entrySet()) {
            try {
                String id = itemFutureEntry.getKey();
                Future<String> itemFuture = itemFutureEntry.getValue();
                String item = itemFuture.get();
                if ("[]".equals(item)) {
                    println("ID为 " + id + " 的种质信息获取为空！");
                    failedIdList.add(id);
                    continue;
                }
                itemList.add(item);
            } catch (InterruptedException | ExecutionException e) {
                println("异步获取数据出错！");
                e.printStackTrace();
                return;
            }
        }

        println("正在进行数据导出...");
        try {
            JSONToExcel.json2Excel(itemList);
        } catch (IOException e) {
            println("数据导出失败！");
            e.printStackTrace();
            return;
        }
        println("数据导出完成。");
        Long end = System.currentTimeMillis();
        println("本次获取种质信息：成功" + itemList.size() + "条，失败" + (itemIdList.size() - itemList.size()) + "条。共耗时：" + (end - start) + "毫秒。");
        println("种质信息获取为空的id列表为： " + JSON.toJSONString(failedIdList));
//        println("亲，觉得满意的话，给个好评哈~ 期待您的下次使用。");
    }

}
