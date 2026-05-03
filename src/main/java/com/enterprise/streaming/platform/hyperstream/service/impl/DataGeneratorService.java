package com.enterprise.streaming.platform.hyperstream.service.impl;

import com.enterprise.streaming.platform.hyperstream.model.*;
import com.enterprise.streaming.platform.hyperstream.repository.*;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataGeneratorService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final WatchlistRepository watchlistRepository;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    private static final int BATCH_SIZE = 5000;

    // Constructor Injection (same style as your MovieServiceImpl)
    public DataGeneratorService(
            MovieRepository movieRepository,
            UserRepository userRepository,
            SubscriptionRepository subscriptionRepository,
            WatchlistRepository watchlistRepository) {

        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.watchlistRepository = watchlistRepository;
    }

    // ---------- SUBSCRIPTIONS ----------
    public String generateSubscriptions(int count) {

        List<Subscription> batch = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            Subscription sub = new Subscription();

            sub.setPlanName(faker.options().option("BASIC","STANDARD","PREMIUM"));
            sub.setPrice(5 + random.nextDouble() * 15);
            sub.setDurationInDays(30 + random.nextInt(335));
            sub.setActive(true);

            batch.add(sub);

            if(batch.size() == BATCH_SIZE){
                subscriptionRepository.saveAll(batch);
                batch.clear();
            }
        }

        if(!batch.isEmpty()){
            subscriptionRepository.saveAll(batch);
        }

        return "Subscriptions inserted";
    }

    // ---------- MOVIES ----------
    public String generateMovies(int count){

        List<Movie> batch = new ArrayList<>();

        for(int i=0;i<count;i++){

            Movie movie = new Movie();

            movie.setTitle(faker.book().title());
            movie.setDescription(faker.lorem().sentence());
            movie.setGenre(faker.book().genre());
            movie.setReleaseYear(1980 + random.nextInt(40));
            movie.setDuration(80 + random.nextInt(120));
            movie.setRating(5 + random.nextDouble() * 5);

            batch.add(movie);

            if(batch.size() == BATCH_SIZE){
                movieRepository.saveAll(batch);
                batch.clear();
            }
        }

        if(!batch.isEmpty()){
            movieRepository.saveAll(batch);
        }

        return "Movies inserted";
    }

    // ---------- USERS ----------
    public String generateUsers(int count){

        List<User> batch = new ArrayList<>();

        List<Subscription> subscriptions = subscriptionRepository.findAll();

        for(int i=0;i<count;i++){

            User user = new User();

            user.setName(faker.name().fullName());
//            user.setEmail(faker.internet().emailAddress());
            user.setEmail("user" + System.currentTimeMillis() + i + "@example.com");
            user.setSubscriptionType(faker.options().option("BASIC","STANDARD","PREMIUM"));

            Subscription randomSub =
                    subscriptions.get(random.nextInt(subscriptions.size()));

            user.setSubscription(randomSub);

            batch.add(user);

            if(batch.size() == BATCH_SIZE){
                userRepository.saveAll(batch);
                batch.clear();
            }
        }

        if(!batch.isEmpty()){
            userRepository.saveAll(batch);
        }

        return "Users inserted";
    }

    // ---------- WATCHLIST ----------
    public String generateWatchlists(int count){

        List<Watchlist> batch = new ArrayList<>();

        List<User> users = userRepository.findAll();
        List<Movie> movies = movieRepository.findAll();

        for(int i=0;i<count;i++){

            User user = users.get(random.nextInt(users.size()));
            Movie movie = movies.get(random.nextInt(movies.size()));

            Watchlist watchlist = new Watchlist(user, movie);

            batch.add(watchlist);

            if(batch.size() == BATCH_SIZE){
                watchlistRepository.saveAll(batch);
                batch.clear();
            }
        }

        if(!batch.isEmpty()){
            watchlistRepository.saveAll(batch);
        }

        return "Watchlist inserted";
    }

}