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
        headers.add("Cookie", "acw_tc=7b39758715720536210983761efd4f8d204476139dbf770161e3e6235c7083; ASPSESSIONIDASSRSQAS=AGHNHGLBDMGCFJBENLBHDOAO; PHPSESSID=5eab48d6d6baaa9e94fcf96e442a5f66; UM_distinctid=16e05b37f71340-08f21b4376bb28-5373e62-1fa400-16e05b37f72a75; CNZZDATA1259170489=1812938470-1572053460-http%253A%252F%252Fwww.cgris.net%252F%7C1572080567");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("action", "item");
        map.add("p", id);
        map.add("croptype", "[\"粮食作物\", \"小麦\"]");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return FontUtil.decodeUnicode(Objects.requireNonNull(response.getBody()));
    }

    @Autowired
    private SeedSearchUtil seedSearchUtil;

    private void println(String string) {
        System.out.println(new Date(System.currentTimeMillis()).toLocaleString() + "  " + string);
    }

    @Override
    public void exportData() {
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

        List<Future<String>> futureList = new ArrayList<>();
        for (String id : itemIdList) {
            Future<String> itemFuture = seedSearchUtil.getItemInfo(id);
            futureList.add(itemFuture);
        }
        println("开始数据爬取...");

        List<String> itemList = new ArrayList<>();
        for (Future<String> itemFuture : futureList) {
            try {
                String item = itemFuture.get();
                if("[]".equals(item)){
                    println("一个种质信息获取失败！");
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
//        println("亲，觉得满意的话，给个好评哈~ 期待您的下次使用。");
    }

}
