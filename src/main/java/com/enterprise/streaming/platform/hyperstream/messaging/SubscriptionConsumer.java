package com.enterprise.streaming.platform.hyperstream.messaging;

import com.enterprise.streaming.platform.hyperstream.config.RabbitMQConfig;
import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import com.enterprise.streaming.platform.hyperstream.model.SubscriptionMessageAudit;
import com.enterprise.streaming.platform.hyperstream.service.impl.SubscriptionMessageAuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionConsumer.class);
    private final SubscriptionMessageAuditService auditService;

    public SubscriptionConsumer(SubscriptionMessageAuditService auditService) {
        this.auditService = auditService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(Subscription subscription) {
        logger.info(
                "Received subscription message. id={}, planName={}, price={}, durationInDays={}, active={}",
                subscription.getId(),
                subscription.getPlanName(),
                subscription.getPrice(),
                subscription.getDurationInDays(),
                subscription.getActive()
        );

        processSubscriptionMessage(subscription);
    }

    private void processSubscriptionMessage(Subscription subscription) {
        SubscriptionMessageAudit audit = auditService.saveProcessedMessage(subscription);
        logger.info(
                "Processed subscription event for planName={}, price={}, auditId={}",
                subscription.getPlanName(),
                subscription.getPrice(),
                audit.getId()
        );
    }
}
