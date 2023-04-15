package com.namnh.peertn.websitecategorizer.service;

import org.ahocorasick.trie.PayloadTrie;

import java.util.*;
import java.util.function.Function;

/*
 * Uses Aho-Corasick algorithm to find keyword matches. Returns immediately when every category has been matched/
 * Time complexity: O(N+M+k)
 *      N: website content length
 *      M: number of categories
 *      k: maximum length of a keyword
 * Space complexity: O(K)
 *      K: sum of length of keywords
 */
public class AhoCorasickStrategy implements IKeywordMatchingStrategy {

    @Override
    public Map<String, Set<String>> matchTextsAgainstCategories(Collection<String> texts, Map<String, Collection<String>> categoryKeywordMap, Function<String, String> textTransformer) {
        int numberOfCategories = categoryKeywordMap.keySet().size();

        PayloadTrie.PayloadTrieBuilder<String> trieBuilder = PayloadTrie.<String>builder().ignoreCase().stopOnHit();
        for (Map.Entry<String, Collection<String>> entry : categoryKeywordMap.entrySet()) {
            String categoryName = entry.getKey();
            Collection<String> categoryKeywords = entry.getValue();

            for (String keyword : categoryKeywords) {
                trieBuilder.addKeyword(keyword, categoryName);
            }
        }
        PayloadTrie<String> categoryKeywordTrie = trieBuilder.build();

        Map<String, Set<String>> ret = new HashMap<>();
        for (String text : texts) {
            String transformedText = textTransformer.apply(text);

            Set<String> categoryNames = new HashSet<>(numberOfCategories);
            categoryKeywordTrie.parseText(transformedText, emit -> {
                categoryNames.add(emit.getPayload());

                // Stop finding keyword matches after every category has been found
                return categoryNames.size() == numberOfCategories;
            });

            ret.put(text, categoryNames);
        }

        return ret;
    }
}
