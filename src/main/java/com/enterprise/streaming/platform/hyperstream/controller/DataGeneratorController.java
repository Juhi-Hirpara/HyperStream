package com.enterprise.streaming.platform.hyperstream.controller;

import com.enterprise.streaming.platform.hyperstream.service.impl.DataGeneratorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dev")
public class DataGeneratorController {

    private final DataGeneratorService dataGeneratorService;

    public DataGeneratorController(DataGeneratorService dataGeneratorService) {
        this.dataGeneratorService = dataGeneratorService;
    }

    @PostMapping("/subscriptions/{count}")
    public String generateSubscriptions(@PathVariable int count){
        return dataGeneratorService.generateSubscriptions(count);
    }

    @PostMapping("/movies/{count}")
    public String generateMovies(@PathVariable int count){
        return dataGeneratorService.generateMovies(count);
    }

    @PostMapping("/users/{count}")
    public String generateUsers(@PathVariable int count){
        return dataGeneratorService.generateUsers(count);
    }

    @PostMapping("/watchlists/{count}")
    public String generateWatchlists(@PathVariable int count){
        return dataGeneratorService.generateWatchlists(count);
    }
}