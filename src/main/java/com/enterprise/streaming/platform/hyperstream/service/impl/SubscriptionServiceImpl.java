package com.enterprise.streaming.platform.hyperstream.service.impl;

import com.enterprise.streaming.platform.hyperstream.dao.api.ISubscriptionDao;
import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import com.enterprise.streaming.platform.hyperstream.service.api.ISubscriptionService;
import com.enterprise.streaming.platform.hyperstream.messaging.SubscriptionProducer;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements ISubscriptionService {

    private final ISubscriptionDao subscriptionDao;
    private final SubscriptionProducer producer;

    public SubscriptionServiceImpl(ISubscriptionDao subscriptionDao,SubscriptionProducer producer) {
        this.subscriptionDao = subscriptionDao;
        this.producer = producer;
    }

    @Override
    @CachePut(value = "subscription", key = "#result.id")
    @CacheEvict(value = "subscription", key = "'all'")
    public Subscription createSubscription(Subscription subscription) {
//        return subscriptionDao.save(subscription);

        Subscription saved = subscriptionDao.save(subscription);
        // 🔥 SEND MESSAGE TO RABBITMQ
        producer.sendSubscriptionMessage(saved);
        return saved;
    }

    @Override
    @Cacheable(value = "subscription", key = "'all'")
    public List<Subscription> getAllSubscriptions() {
        return subscriptionDao.findAll();
    }

    @Override
    @Cacheable(value = "subscription", key = "#id")
    public Subscription getSubscriptionById(Long id) {
        return subscriptionDao.findById(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "subscription", key = "#id"),
            @CacheEvict(value = "subscription", key = "'all'")
    })
    public void deleteSubscription(Long id) {
        subscriptionDao.deleteById(id);
    }
}