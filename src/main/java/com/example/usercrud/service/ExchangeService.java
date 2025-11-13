package com.example.usercrud.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeService {

    private final RestTemplate restTemplate = new RestTemplate();

    // The remote URL to fetch exchange info (provided by user)
    private final String exchangeUrl = "https://m.search.naver.com/p/csearch/content/qapirender.nhn?key=calculator&pkid=141&q=%ED%99%98%EC%9C%A8&where=m&u1=keb&u6=standardUnit&u7=0&u3=JPY&u4=KRW&u8=down&u2=100";

    /**
     * Fetches the raw JSON/string response from the remote URL.
     */
    public String fetchExchangeRaw() {
        return restTemplate.getForObject(exchangeUrl, String.class);
    }
}
