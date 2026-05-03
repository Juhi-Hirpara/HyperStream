package com.enterprise.streaming.platform.hyperstream.service.impl;

import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import com.enterprise.streaming.platform.hyperstream.model.SubscriptionMessageAudit;
import com.enterprise.streaming.platform.hyperstream.repository.SubscriptionMessageAuditRepository;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionMessageAuditService {

    private final SubscriptionMessageAuditRepository auditRepository;

    public SubscriptionMessageAuditService(SubscriptionMessageAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public SubscriptionMessageAudit saveProcessedMessage(Subscription subscription) {
        SubscriptionMessageAudit audit = new SubscriptionMessageAudit();
        audit.setSubscriptionId(subscription.getId());
        audit.setPlanName(subscription.getPlanName());
        audit.setPrice(subscription.getPrice());
        audit.setDurationInDays(subscription.getDurationInDays());
        audit.setActive(subscription.getActive());
        audit.setProcessingStatus("PROCESSED");
        return auditRepository.save(audit);
    }
}
