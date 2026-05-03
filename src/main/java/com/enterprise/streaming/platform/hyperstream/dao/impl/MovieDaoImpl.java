package com.enterprise.streaming.platform.hyperstream.dao.impl;

import com.enterprise.streaming.platform.hyperstream.dao.api.IMovieDao;
import com.enterprise.streaming.platform.hyperstream.model.Movie;
import com.enterprise.streaming.platform.hyperstream.repository.MovieRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieDaoImpl implements IMovieDao {

    private final MovieRepository movieRepository;

    public MovieDaoImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    @Override
    public Movie update(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }
}