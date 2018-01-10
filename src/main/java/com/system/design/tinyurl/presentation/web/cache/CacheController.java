package com.system.design.tinyurl.presentation.web.cache;

import com.google.gson.Gson;
import com.system.design.tinyurl.application.cache.CacheService;
import com.system.design.tinyurl.domain.cache.query.OriginalUrlByHashQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class CacheController {

    private final CacheService cacheService;
    private final Gson gson;

    @Autowired
    public CacheController(CacheService cacheService, Gson gson) {
        this.cacheService = cacheService;
        this.gson = gson;
    }

    @GetMapping(path = "/cache/{urlHash}")
    public RedirectView getOriginalUrlByHash(@PathVariable String urlHash) {
        return cacheService.getOriginalUrlByHash(new OriginalUrlByHashQuery(urlHash))
                .map(RedirectView::new)
                .orElse(new RedirectView("/404.html", true));
    }
}
