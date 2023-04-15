package com.namnh.peertn.websitecategorizer.service;

import com.namnh.peertn.websitecategorizer.config.DefaultConfig;
import com.namnh.peertn.websitecategorizer.model.WebCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/*
 * Extracts text from a website and determine if a website contains keywords of some category.
 */
@Service
public class WebTextService {

    private final Runner runner;

    @Autowired
    WebTextService(Runner runner) {
        this.runner = runner;
    }

    public String extractWebsiteText(String url) {
        return runner.extractWebText(url);
    }

    public Map<String, Set<String>> categorizeWebsites(Collection<String> urls) {
        return categorizeWebsites(urls, DefaultConfig.DEFAULT_WEB_CATEGORIES);
    }

    public Map<String, Set<String>> categorizeWebsites(Collection<String> urls, Collection<WebCategory> webCategories) {
        return runner.categorizeWebsites(urls, webCategories);
    }
}
