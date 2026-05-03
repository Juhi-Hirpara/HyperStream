package com.enterprise.streaming.platform.hyperstream.service.api;

import com.enterprise.streaming.platform.hyperstream.model.Watchlist;
import java.util.List;

public interface IWatchlistService {

    Watchlist addToWatchlist(Long userId, Long movieId);
    List<Watchlist> getUserWatchlist(Long userId);
    void removeFromWatchlist(Long id);
}