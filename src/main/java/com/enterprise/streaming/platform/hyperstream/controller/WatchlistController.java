package com.enterprise.streaming.platform.hyperstream.controller;

import com.enterprise.streaming.platform.hyperstream.model.Watchlist;
import com.enterprise.streaming.platform.hyperstream.service.api.IWatchlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final IWatchlistService watchlistService;

    public WatchlistController(IWatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping
    public Watchlist addToWatchlist(@RequestParam Long userId,
                                    @RequestParam Long movieId) {
        return watchlistService.addToWatchlist(userId, movieId);
    }

    @GetMapping("/{userId}")
    public List<Watchlist> getUserWatchlist(@PathVariable Long userId) {
        return watchlistService.getUserWatchlist(userId);
    }

    @DeleteMapping("/{id}")
    public String removeFromWatchlist(@PathVariable Long id) {
        watchlistService.removeFromWatchlist(id);
        return "Removed from watchlist";
    }
}