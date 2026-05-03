package com.enterprise.streaming.platform.hyperstream.dao.api;

import com.enterprise.streaming.platform.hyperstream.model.Watchlist;
import java.util.List;

public interface IWatchlistDao {

    Watchlist save(Watchlist watchlist);
    List<Watchlist> findByUserId(Long userId);
    void deleteById(Long id);
}