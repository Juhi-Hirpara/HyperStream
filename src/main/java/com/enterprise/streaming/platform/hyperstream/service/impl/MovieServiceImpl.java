package com.enterprise.streaming.platform.hyperstream.service.impl;

import com.enterprise.streaming.platform.hyperstream.dao.api.IMovieDao;
import com.enterprise.streaming.platform.hyperstream.model.Movie;
import com.enterprise.streaming.platform.hyperstream.service.api.IMovieService;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements IMovieService {

    private final IMovieDao movieDao;

    public MovieServiceImpl(IMovieDao movieDao) {
        this.movieDao = movieDao;
    }

    @Override
    @CachePut(value = "movies", key = "#result.id")
    @CacheEvict(value = "movies", key = "'all'")
    public Movie createMovie(Movie movie) {
        return movieDao.save(movie);
    }

    @Override
    @Cacheable(value = "movies", key = "'all'" ,sync = true)
    public List<Movie> getAllMovies() {
        return movieDao.findAll();
    }

    @Override
    @Cacheable(value = "movies", key = "#id")
    public Movie getMovieById(Long id) {
        return movieDao.findById(id);
    }

    @Override
    @CachePut(value = "movies", key = "#id")
    @CacheEvict(value = "movies", key = "'all'")
    public Movie updateMovie(Long id, Movie movie) {
        Movie existing = movieDao.findById(id);
        existing.setTitle(movie.getTitle());
        existing.setDescription(movie.getDescription());
        existing.setGenre(movie.getGenre());
        existing.setReleaseYear(movie.getReleaseYear());
        existing.setDuration(movie.getDuration());
        existing.setRating(movie.getRating());
        return movieDao.update(existing);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "movies", key = "#id"),
            @CacheEvict(value = "movies", key = "'all'")
    })
    public void deleteMovie(Long id) {
        movieDao.deleteById(id);
    }
}