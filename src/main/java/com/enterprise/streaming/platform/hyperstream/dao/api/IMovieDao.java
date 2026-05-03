package com.enterprise.streaming.platform.hyperstream.dao.api;

import com.enterprise.streaming.platform.hyperstream.model.Movie;
import java.util.List;

public interface IMovieDao {
    Movie save(Movie movie);
    List<Movie> findAll();
    Movie findById(Long id);
    Movie update(Movie movie);
    void deleteById(Long id);
}