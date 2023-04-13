package com.namnh.peertn.websitecategorizer.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebTextService {

    public String extractWebText(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return _cleanWebText(document.text());
    }

    private String _cleanWebText(String rawWebText) {
        return Jsoup.parse(rawWebText).text();
    }
}
