package com.system.design.tinyurl.presentation.web.url;

import com.google.gson.Gson;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.domain.url.query.TinyUrlByIdQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class TinyUrlController {

    private TinyUrlService tinyUrlService;
    private Gson gson;

    @Autowired
    public TinyUrlController(TinyUrlService tinyUrlService, Gson gson) {
        this.tinyUrlService = tinyUrlService;
        this.gson = gson;
    }

    @PostMapping(path = "/tinyurl")
    public ResponseEntity createTinyUrl(@RequestBody String body) {
        CreateTinyUrlCommand createTinyUrlCommand = gson.fromJson(body, CreateTinyUrlCommand.class);
        tinyUrlService.createTinyUrl(createTinyUrlCommand);
        return new ResponseEntity(OK);
    }

    @PostMapping(path = "/tinyurl/{tinyUrlUuid}")
    public ResponseEntity<String> getTinyUrlById(@PathVariable String tinyUrlUuid) {
        return tinyUrlService.getTinyUrlById(new TinyUrlByIdQuery(new TinyUrlId(tinyUrlUuid)))
                .map(tinyUrl -> new ResponseEntity<>(gson.toJson(tinyUrl), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
