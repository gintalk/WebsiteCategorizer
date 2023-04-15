package com.namnh.peertn.websitecategorizer.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@FunctionalInterface
public interface IKeywordMatchingStrategy {

    Map<String, Set<String>> matchTextsAgainstCategories(Collection<String> texts, Map<String, Collection<String>> categoryKeywordMap, Function<String, String> textTransformer);
}
