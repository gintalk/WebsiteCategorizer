package com.namnh.peertn.websitecategorizer.service;

import com.namnh.peertn.websitecategorizer.config.DefaultConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

/**
 * Extracts text from a website and determine if a website content contains keywords of some category.
 */
@Service
public class WebTextService {

    private final Runner runner;

    @Autowired
    WebTextService(Runner runner) {
        this.runner = runner;
    }

    public String extractWebText(String url) throws IOException {
        return runner.extractWebText(url);
    }

    public Set<String> categorizeWeb(String url) throws IOException {
        return runner.categorizeWebsite(url, DefaultConfig.DEFAULT_WEB_CATEGORIES);
    }
}
