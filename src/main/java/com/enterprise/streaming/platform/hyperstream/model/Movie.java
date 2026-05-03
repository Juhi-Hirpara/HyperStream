package com.enterprise.streaming.platform.hyperstream.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "movies")
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String genre;
    private Integer releaseYear;
    private Integer duration; // in minutes
    private Double rating;

    public Movie() {}

    public Movie(Long id, String title, String description, String genre,
                 Integer releaseYear, Integer duration, Double rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.rating = rating;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}