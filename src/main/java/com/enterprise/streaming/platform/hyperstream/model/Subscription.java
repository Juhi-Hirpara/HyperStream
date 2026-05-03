package com.enterprise.streaming.platform.hyperstream.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "subscriptions")
public class Subscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;        // BASIC / STANDARD / PREMIUM
    private Double price;
    private Integer durationInDays;
    private Boolean active;

    public Subscription() {}

    public Subscription(String planName, Double price, Integer durationInDays, Boolean active) {
        this.planName = planName;
        this.price = price;
        this.durationInDays = durationInDays;
        this.active = active;
    }

    public Long getId() { return id; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getDurationInDays() { return durationInDays; }
    public void setDurationInDays(Integer durationInDays) { this.durationInDays = durationInDays; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}