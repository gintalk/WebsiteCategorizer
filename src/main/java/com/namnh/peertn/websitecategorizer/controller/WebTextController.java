package com.namnh.peertn.websitecategorizer.controller;

import com.namnh.peertn.websitecategorizer.service.WebTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class WebTextController {

    private final WebTextService service;

    @Autowired
    WebTextController(WebTextService service) {
        this.service = service;
    }

    @GetMapping("/web-texts")
    ResponseEntity<String> extractWebText(@RequestParam String url) throws IOException {
        return ResponseEntity.ok(service.extractWebText(url));
    }
}
