package com.enterprise.streaming.platform.hyperstream.messaging;

import com.enterprise.streaming.platform.hyperstream.config.RabbitMQConfig;
import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionProducer {

    private final RabbitTemplate rabbitTemplate;

    public SubscriptionProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSubscriptionMessage(Subscription subscription) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                subscription
        );
    }
}