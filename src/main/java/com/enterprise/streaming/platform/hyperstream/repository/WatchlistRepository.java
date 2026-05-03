package com.enterprise.streaming.platform.hyperstream.repository;

import com.enterprise.streaming.platform.hyperstream.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    List<Watchlist> findByUserId(Long userId);
}