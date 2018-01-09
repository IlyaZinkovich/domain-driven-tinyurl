package com.system.design.tinyurl.presentation.web.cache;

import com.google.gson.Gson;
import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.domain.cache.query.OriginalUrlByHashQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class CacheController {

    private CacheService cacheService;
    private Gson gson;

    @Autowired
    public CacheController(CacheService cacheService, Gson gson) {
        this.cacheService = cacheService;
        this.gson = gson;
    }

    @GetMapping(path = "/cache/{urlHash}")
    public ResponseEntity<String> getOriginalUrlByHash(@PathVariable String urlHash) {
        return cacheService.getOriginalUrlByHash(new OriginalUrlByHashQuery(urlHash))
                .map(originalUrl -> new ResponseEntity<>(gson.toJson(new OriginalUrlResponse(originalUrl)), OK))
                .orElse(new ResponseEntity<>("", NOT_FOUND));
    }
}
