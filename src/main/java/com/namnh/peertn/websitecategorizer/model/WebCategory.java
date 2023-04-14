package com.namnh.peertn.websitecategorizer.model;

import java.util.Collections;
import java.util.List;

public class WebCategory {

    private final String name;
    private final List<String> keywords;

    public WebCategory(String name, List<String> keywords) {
        this.name = name;
        this.keywords = Collections.unmodifiableList(keywords);
    }

    public String getName() {
        return this.name;
    }

    public List<String> getKeywords() {
        return this.keywords;
    }
}
