package com.enterprise.streaming.platform.hyperstream.dao.api;

import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import java.util.List;

public interface ISubscriptionDao {

    Subscription save(Subscription subscription);
    List<Subscription> findAll();
    Subscription findById(Long id);
    void deleteById(Long id);
}