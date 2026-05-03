package com.enterprise.streaming.platform.hyperstream.repository;

import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}