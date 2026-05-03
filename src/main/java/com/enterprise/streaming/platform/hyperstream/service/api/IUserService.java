package com.enterprise.streaming.platform.hyperstream.service.api;

import com.enterprise.streaming.platform.hyperstream.model.User;

import java.util.List;

public interface IUserService {

    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}