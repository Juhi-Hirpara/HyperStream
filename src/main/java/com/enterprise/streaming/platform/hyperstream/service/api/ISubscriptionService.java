package com.enterprise.streaming.platform.hyperstream.service.api;

import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import java.util.List;

public interface ISubscriptionService {

    Subscription createSubscription(Subscription subscription);
    List<Subscription> getAllSubscriptions();
    Subscription getSubscriptionById(Long id);
    void deleteSubscription(Long id);
}