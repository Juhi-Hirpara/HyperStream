package com.enterprise.streaming.platform.hyperstream.dao.impl;

import com.enterprise.streaming.platform.hyperstream.dao.api.IWatchlistDao;
import com.enterprise.streaming.platform.hyperstream.model.Watchlist;
import com.enterprise.streaming.platform.hyperstream.repository.WatchlistRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WatchlistDaoImpl implements IWatchlistDao {

    private final WatchlistRepository watchlistRepository;

    public WatchlistDaoImpl(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    @Override
    public Watchlist save(Watchlist watchlist) {
        return watchlistRepository.save(watchlist);
    }

    @Override
    public List<Watchlist> findByUserId(Long userId) {
        return watchlistRepository.findByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        watchlistRepository.deleteById(id);
    }
}