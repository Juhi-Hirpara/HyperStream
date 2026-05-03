package com.enterprise.streaming.platform.hyperstream.controller;

import com.enterprise.streaming.platform.hyperstream.model.Subscription;
import com.enterprise.streaming.platform.hyperstream.service.api.ISubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final ISubscriptionService subscriptionService;

    public SubscriptionController(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public Subscription createSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.createSubscription(subscription);
    }

    @GetMapping
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @GetMapping("/{id}")
    public Subscription getSubscriptionById(@PathVariable Long id) {
        return subscriptionService.getSubscriptionById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return "Subscription deleted successfully";
    }
}