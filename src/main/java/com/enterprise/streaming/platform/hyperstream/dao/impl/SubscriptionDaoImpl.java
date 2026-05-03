package com.enterprise.streaming.platform.hyperstream.dao.impl;

import com.enterprise.streaming.platform.hyperstream.dao.api.ISubscriptionDao;
import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import com.enterprise.streaming.platform.hyperstream.repository.SubscriptionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubscriptionDaoImpl implements ISubscriptionDao {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionDaoImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        subscriptionRepository.deleteById(id);
    }
}