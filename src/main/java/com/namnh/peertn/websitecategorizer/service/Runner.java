package com.namnh.peertn.websitecategorizer.service;

import com.namnh.peertn.websitecategorizer.model.WebCategory;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Runner {

    // A cache that stores the result of website categorization, because parsing website text is expensive
    private final Cache<String, Set<String>> webCategoryCache;
    private final Cache<String, String> webTextCache;

    Runner() {
        webCategoryCache = new Cache2kBuilder<String, Set<String>>() {
        }
                .entryCapacity(1000)
                .expireAfterWrite(Duration.ofHours(24))     // Cached items live for 24 hours since website content rarely changes
                .build();

        webTextCache = new Cache2kBuilder<String, String>() {
        }
                .entryCapacity(1000)
                .expireAfterWrite(Duration.ofHours(24))     // Cached items live for 24 hours since website content rarely changes
                .build();
    }

    public String extractWebText(String url) {
        String cached = webTextCache.get(url);
        if (!ObjectUtils.isEmpty(cached)) {
            return cached;
        }

        try {
            Document document = Jsoup.connect(url).get();
            String webText = _cleanWebText(document.text());

            if (!ObjectUtils.isEmpty(webText)) {
                webTextCache.put(url, webText);
            }

            return webText;
        } catch (IOException e) {
            return "";
        }
    }

    public Map<String, Set<String>> categorizeWebsites(Collection<String> urls, Collection<WebCategory> categories) {
        return categorizeWebsites(urls, categories, new AhoCorasickStrategy());
    }

    public Map<String, Set<String>> categorizeWebsites(Collection<String> urls, Collection<WebCategory> categories, IKeywordMatchingStrategy matchingStrategy) {
        Map<String, Set<String>> ret = new HashMap<>();

        Map<String, Collection<String>> categoryKeywordMap = categories.stream()
                .collect(Collectors.toMap(
                        WebCategory::getName,
                        WebCategory::getKeywords
                ));

        // If any URL categorization has been done before and its result is still in the cache, use it. Else, put the
        // URL in a list to be processed.
        Collection<String> nonCachedURLs = new ArrayList<>();
        for (String url : urls) {
            Set<String> cached = webCategoryCache.get(url);
            if (ObjectUtils.isEmpty(cached)) {
                nonCachedURLs.add(url);
            } else {
                ret.put(url, cached);
            }
        }

        Map<String, Set<String>> strategyResult = matchingStrategy.matchTextsAgainstCategories(nonCachedURLs, categoryKeywordMap, this::extractWebText);

        // Iterates the result from the matching strategy, puts them both in the cache and in the returned map.
        for (Map.Entry<String, Set<String>> entry : strategyResult.entrySet()) {
            String nonCachedURL = entry.getKey();
            Set<String> freshlyComputedCategories = entry.getValue();

            ret.put(nonCachedURL, freshlyComputedCategories);
            webCategoryCache.put(nonCachedURL, freshlyComputedCategories);
        }

        return ret;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private String _cleanWebText(String rawWebText) {
        return Jsoup.parse(rawWebText).text();
    }
}
