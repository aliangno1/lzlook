package com.lzlook.backend.controller;

import com.lzlook.backend.service.SeedFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("seed")
public class SeedController {

    @Autowired
    private SeedFetchService seedFetchService;

    @RequestMapping("")
    public String seed() {
        return "seed";
    }

    @RequestMapping("/search")
    @ResponseBody
    public String search(@RequestParam String id, Model model) {
//        ListResponse<> response = new ListResponse<>();
//        Long start = System.currentTimeMillis();
        String item = seedFetchService.getItemInfo(id);
//        Long end = System.currentTimeMillis();
//        System.out.println("search cost time:" + (end - start) + "ms");
        System.out.println(item);
//        response.success(list);
//        model.addAttribute("results", list);
//        return "seed-results";
        return item;
    }

    @RequestMapping("/export")
    @ResponseBody
    public void export(@RequestParam String key1, @RequestParam String key2, Model model) {
        seedFetchService.exportData(key1,key2);
    }
}
