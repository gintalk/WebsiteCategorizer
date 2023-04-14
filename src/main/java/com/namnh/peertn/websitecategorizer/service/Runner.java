package com.namnh.peertn.websitecategorizer.service;

import com.namnh.peertn.websitecategorizer.model.WebCategory;
import org.ahocorasick.trie.PayloadTrie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class Runner {

    public String extractWebText(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return _cleanWebText(document.text());
    }

    public Set<String> categorizeWebsite(String url, List<WebCategory> categories) throws IOException {
        return categorizeWebsites(Collections.singletonList(url), categories).get(url);
    }

    /*
     * Uses Aho-Corasick algorithm to find keyword matches. Returns immediately when every category has been matched/
     * Time complexity: O(N+M+k)
     *      N: website content length
     *      M: number of categories
     *      k: maximum length of a keyword
     * Space complexity: O(K)
     *      K: sum of length of keywords
     */
    public Map<String, Set<String>> categorizeWebsites(List<String> urls, List<WebCategory> categories) throws IOException {
        PayloadTrie.PayloadTrieBuilder<String> trieBuilder = PayloadTrie.<String>builder().ignoreCase().stopOnHit();
        for (WebCategory webCategory : categories) {
            for (String keyword : webCategory.getKeywords()) {
                trieBuilder.addKeyword(keyword, webCategory.getName());
            }
        }
        PayloadTrie<String> categoryKeywordTrie = trieBuilder.build();

        Map<String, Set<String>> ret = new HashMap<>();
        for (String url : urls) {
            String webText = extractWebText(url);

            Set<String> webCategories = new HashSet<>(categories.size());
            categoryKeywordTrie.parseText(webText, emit -> {
                webCategories.add(emit.getPayload());

                // Stop finding keyword matches after every category has been found
                return webCategories.size() == categories.size();
            });

            ret.put(url, webCategories);
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
