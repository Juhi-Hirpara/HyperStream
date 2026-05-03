# RabbitMQ Old Vs New Plan

This file documents the **current RabbitMQ code** in the project and the **planned safe changes** before implementation.

The goal is:
- keep existing application flow intact
- avoid affecting unrelated backend code
- replace `System.out.println` in the consumer with actual application-safe handling

## Current RabbitMQ Setup

### 1. App Entry Point

File: `src/main/java/com/enterprise/streaming/platform/hyperstream/HyperStreamApplication.java`

Current code:

```java
package com.enterprise.streaming.platform.hyperstream;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
@EnableRabbit
public class HyperStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyperStreamApplication.class, args);
	}

}
```

Current behavior:
- Rabbit listeners are enabled through `@EnableRabbit`
- no issue here

## 2. RabbitMQ Configuration

File: `src/main/java/com/enterprise/streaming/platform/hyperstream/config/RabbitMQConfig.java`

Current code:

```java
package com.enterprise.streaming.platform.hyperstream.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "subscription.queue";
    public static final String EXCHANGE = "subscription.exchange";
    public static final String ROUTING_KEY = "subscription.routingKey";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());

        return factory;
    }
}
```

Current behavior:
- queue, exchange, and binding already exist
- JSON message conversion is already configured
- producer and consumer can exchange `Subscription` objects

## 3. Current Producer

File: `src/main/java/com/enterprise/streaming/platform/hyperstream/messaging/SubscriptionProducer.java`

Current code:

```java
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
```

Current behavior:
- sends `Subscription` object to RabbitMQ after subscription creation
- this is already valid and should remain mostly unchanged

## 4. Current Consumer

File: `src/main/java/com/enterprise/streaming/platform/hyperstream/messaging/SubscriptionConsumer.java`

Current code:

```java
package com.enterprise.streaming.platform.hyperstream.messaging;

import com.enterprise.streaming.platform.hyperstream.config.RabbitMQConfig;
import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(Subscription subscription) {

        // 🔥 Simulate email sending
        System.out.println("📩 Sending Welcome Email...");
        System.out.println("Plan: " + subscription.getPlanName());
        System.out.println("Price: " + subscription.getPrice());

        // 👉 Later you can integrate JavaMailSender here
    }
}
```

Current behavior:
- consumes subscription messages successfully
- only prints to console using `System.out.println`
- does not use proper application logging
- does not perform real post-processing beyond console output

## Problem In Current Consumer

Current issue:
- `System.out.println` is not ideal in production code
- logs are not structured
- future processing is not isolated
- no dedicated method for real business action after receiving message

## Planned New Code

The new version will be designed to avoid affecting unrelated code.

### Planned changes in consumer

We will:
- remove `System.out.println`
- use Spring-friendly logging with `Logger` / `LoggerFactory`
- keep `@RabbitListener` exactly in place
- keep queue name unchanged
- keep message type as `Subscription`
- add a small internal method for actual processing
- make consumer easier to extend later for email, notification, audit, or DB action

### Planned consumer flow

Old flow:
- receive message
- print to console

New flow:
- receive message
- log subscription receipt
- call a dedicated processing method
- log success or safe failure

## What Will Not Change

These parts should remain stable:
- `SubscriptionServiceImpl` message sending flow
- `SubscriptionProducer` exchange/routing usage
- `RabbitMQConfig` queue/exchange/binding names
- backend subscription CRUD behavior
- unrelated controllers, services, repositories, or DAOs

## Safe Implementation Direction

The first safe implementation should be:

1. Keep producer logic as-is
2. Update consumer only
3. Replace console prints with logger
4. Add a dedicated handler method such as:

```java
private void processSubscriptionMessage(Subscription subscription)
```

5. Log useful fields like:
- subscription id
- plan name
- price
- active status

## Summary

Older code status:
- RabbitMQ is already integrated
- producer is already sending messages
- consumer is listening correctly
- main missing improvement is replacing console printing with real processing/logging

Next step after this file:
- implement the improved consumer safely without breaking any other code
