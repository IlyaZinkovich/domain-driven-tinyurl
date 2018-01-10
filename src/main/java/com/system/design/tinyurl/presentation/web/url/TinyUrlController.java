package com.system.design.tinyurl.presentation.web.url;

import com.google.gson.Gson;
import com.system.design.tinyurl.application.url.TinyUrlService;
import com.system.design.tinyurl.domain.event.DomainEventsSubscriber;
import com.system.design.tinyurl.domain.url.TinyUrlCreatedEvent;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.command.CreateTinyUrlCommand;
import com.system.design.tinyurl.domain.url.query.TinyUrlByIdQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class TinyUrlController {

    private TinyUrlService tinyUrlService;
    private Gson gson;
    private DomainEventsSubscriber domainEventsSubscriber;

    @Autowired
    public TinyUrlController(TinyUrlService tinyUrlService, Gson gson,
                             @Qualifier("tinyUrlDomainEventsSubscriber") DomainEventsSubscriber domainEventsSubscriber) {
        this.tinyUrlService = tinyUrlService;
        this.gson = gson;
        this.domainEventsSubscriber = domainEventsSubscriber;
    }

    @PostMapping(path = "/tinyurl")
    public ResponseEntity<String> createTinyUrl(@RequestBody String body) {
        CreateTinyUrlCommand createTinyUrlCommand = gson.fromJson(body, CreateTinyUrlCommand.class);
        TinyUrlId tinyUrlId = tinyUrlService.createTinyUrl(createTinyUrlCommand);
        return new ResponseEntity<>(gson.toJson(tinyUrlId), OK);
    }

    @GetMapping(path = "/tinyurl/{tinyUrlUuid}")
    public ResponseEntity<String> getTinyUrlById(@PathVariable String tinyUrlUuid) {
        return tinyUrlService.getTinyUrlById(new TinyUrlByIdQuery(new TinyUrlId(tinyUrlUuid)))
                .map(tinyUrl -> new ResponseEntity<>(gson.toJson(tinyUrl), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping(path = "/tinyurl/events/{tinyUrlUuid}")
    public SseEmitter subscribeToTinyUrlCreationEventsStream(@PathVariable String tinyUrlUuid) {
        SseEmitter sseEmitter = new SseEmitter();
        domainEventsSubscriber.subscribe(domainEvent -> {
            if (domainEvent instanceof TinyUrlCreatedEvent) {
                TinyUrlCreatedEvent tinyUrlCreatedEvent = (TinyUrlCreatedEvent) domainEvent;
                if (tinyUrlCreatedEvent.tinyUrlId().equals(new TinyUrlId(tinyUrlUuid))) {
                    try {
                        sseEmitter.send(gson.toJson(tinyUrlCreatedEvent));
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }
            }
        });
        return sseEmitter;
    }
}
