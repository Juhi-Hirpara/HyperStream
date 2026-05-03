package com.enterprise.streaming.platform.hyperstream.service.impl;

import com.enterprise.streaming.platform.hyperstream.dao.api.IWatchlistDao;
import com.enterprise.streaming.platform.hyperstream.model.Movie;
import com.enterprise.streaming.platform.hyperstream.model.User;
import com.enterprise.streaming.platform.hyperstream.model.Watchlist;
import com.enterprise.streaming.platform.hyperstream.service.api.IMovieService;
import com.enterprise.streaming.platform.hyperstream.service.api.IUserService;
import com.enterprise.streaming.platform.hyperstream.service.api.IWatchlistService;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistServiceImpl implements IWatchlistService {

    private final IWatchlistDao watchlistDao;
    private final IUserService userService;
    private final IMovieService movieService;

    public WatchlistServiceImpl(IWatchlistDao watchlistDao,
                                IUserService userService,
                                IMovieService movieService) {
        this.watchlistDao = watchlistDao;
        this.userService = userService;
        this.movieService = movieService;
    }

    @Override
    @CacheEvict(value = "watchlist", key = "#userId")
    public Watchlist addToWatchlist(Long userId, Long movieId) {
        User user = userService.getUserById(userId);
        Movie movie = movieService.getMovieById(movieId);
        Watchlist watchlist = new Watchlist(user, movie);
        return watchlistDao.save(watchlist);
    }

    @Override
    @Cacheable(value = "watchlist", key = "#userId")
    public List<Watchlist> getUserWatchlist(Long userId) {
        return watchlistDao.findByUserId(userId);
    }

    @Override
    @CacheEvict(value = "watchlist", key = "#id")
    public void removeFromWatchlist(Long id) {
        watchlistDao.deleteById(id);
    }
}