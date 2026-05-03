package com.enterprise.streaming.platform.hyperstream.dao.api;

import com.enterprise.streaming.platform.hyperstream.model.User;

import java.util.List;

public interface IUserDao {

    User save(User user);

    List<User> findAll();

    User findById(Long id);

    User update(User user);

    void deleteById(Long id);
}