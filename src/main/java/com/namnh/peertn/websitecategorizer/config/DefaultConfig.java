package com.namnh.peertn.websitecategorizer.config;

import com.namnh.peertn.websitecategorizer.model.WebCategory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DefaultConfig {

    public static final Collection<WebCategory> DEFAULT_WEB_CATEGORIES = Collections.unmodifiableList(Arrays.asList(
            new WebCategory("Star Wars", Arrays.asList("star war", "starwars", "starwar", "starwars", "r2d2", "may the force be with you")),
            new WebCategory("Basketball", Arrays.asList("basketball", "nba", "ncaa", "lebron james", "john stokton", "anthony davis"))
    ));
}
