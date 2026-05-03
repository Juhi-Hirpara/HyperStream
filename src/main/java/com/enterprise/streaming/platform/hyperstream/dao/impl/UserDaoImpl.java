package com.enterprise.streaming.platform.hyperstream.dao.impl;

import com.enterprise.streaming.platform.hyperstream.dao.api.IUserDao;

import com.enterprise.streaming.platform.hyperstream.model.User;
import com.enterprise.streaming.platform.hyperstream.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements IUserDao {

    private final UserRepository userRepository;

    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}