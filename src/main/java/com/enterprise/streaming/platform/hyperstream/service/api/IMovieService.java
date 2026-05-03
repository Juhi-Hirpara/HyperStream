package com.enterprise.streaming.platform.hyperstream.service.api;

import com.enterprise.streaming.platform.hyperstream.model.Movie;
import java.util.List;

public interface IMovieService {
    Movie createMovie(Movie movie);
    List<Movie> getAllMovies();
    Movie getMovieById(Long id);
    Movie updateMovie(Long id, Movie movie);
    void deleteMovie(Long id);
}