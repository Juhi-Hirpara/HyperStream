package com.enterprise.streaming.platform.hyperstream.service.impl;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.enterprise.streaming.platform.hyperstream.dao.api.IUserDao;
import com.enterprise.streaming.platform.hyperstream.model.User;
import com.enterprise.streaming.platform.hyperstream.service.api.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserDao userDao;

    public UserServiceImpl(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @CacheEvict(value = "users", key = "'all'")
    public User createUser(User user) {
        return userDao.save(user);
    }

    @Override
    @Cacheable(value = "users", key = "'all'")
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        System.out.println("Fetching user from DB...");
        return userDao.findById(id);
    }

    @Override
    @CachePut(value = "users", key = "#id")
    @CacheEvict(value = "users", key = "'all'")
    public User updateUser(Long id, User updatedUser) {

        User existingUser = userDao.findById(id);

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setSubscriptionType(updatedUser.getSubscriptionType());

        return userDao.update(existingUser);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "users", key = "'all'")
    })
    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }
}