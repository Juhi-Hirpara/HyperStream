package com.enterprise.streaming.platform.hyperstream.repository;

import com.enterprise.streaming.platform.hyperstream.model.SubscriptionMessageAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionMessageAuditRepository extends JpaRepository<SubscriptionMessageAudit, Long> {
}
