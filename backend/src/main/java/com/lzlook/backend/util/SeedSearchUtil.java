package com.lzlook.backend.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Future;

@Service
public class SeedSearchUtil {

    private RestTemplate restTemplate = new RestTemplate();

    private static final String url = "http://www.cgris.net/query/o.php";

    private void println(String string){
        System.out.println(new Date(System.currentTimeMillis()).toLocaleString() + "  " + string);
    }

    @Async
    public Future<String> getItemInfo(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "acw_tc=7b39758715720536210983761efd4f8d204476139dbf770161e3e6235c7083; ASPSESSIONIDASSRSQAS=AGHNHGLBDMGCFJBENLBHDOAO; PHPSESSID=5eab48d6d6baaa9e94fcf96e442a5f66; UM_distinctid=16e05b37f71340-08f21b4376bb28-5373e62-1fa400-16e05b37f72a75; CNZZDATA1259170489=1812938470-1572053460-http%253A%252F%252Fwww.cgris.net%252F%7C1572080567");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("action", "item");
        map.add("p", id);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return new AsyncResult<>(FontUtil.decodeUnicode(Objects.requireNonNull(response.getBody())));
    }
}
