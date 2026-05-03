package com.enterprise.streaming.platform.hyperstream.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "watchlist")
public class Watchlist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    private LocalDateTime addedAt;

    public Watchlist() {
        this.addedAt = LocalDateTime.now();
    }

    public Watchlist(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
        this.addedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public LocalDateTime getAddedAt() { return addedAt; }
}