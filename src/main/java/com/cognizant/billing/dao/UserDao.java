package com.cognizant.billing.dao;

import com.cognizant.billing.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user);                 // insert and return with generated id
    int update(User user);                // return rows updated
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean deleteById(Long id);
}