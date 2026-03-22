package com.cognizant.billing.service;

import com.cognizant.billing.dao.UserDao;
import com.cognizant.billing.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserDao userDao;

    private static final Pattern EMAIL_RE = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User add(String name, String email) {
        validateName(name);
        validateEmail(email);
        userDao.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("Email already exists");
        });
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        return userDao.save(u);
    }

    public List<User> list() {
        return userDao.findAll();
    }

    public User update(Long id, String name, String email) {
        if (id == null) throw new IllegalArgumentException("id required");
        User existing = userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (name != null && !name.isBlank()) {
            validateName(name);
            existing.setName(name);
        }
        if (email != null && !email.isBlank()) {
            validateEmail(email);
            if (!email.equalsIgnoreCase(existing.getEmail())) {
                userDao.findByEmail(email).ifPresent(u -> {
                    throw new IllegalArgumentException("Email already exists");
                });
            }
            existing.setEmail(email);
        }

        int rows = userDao.update(existing);
        if (rows == 0) throw new IllegalStateException("No rows updated");
        return existing;
    }

    public boolean delete(Long id) {
        if (id == null) throw new IllegalArgumentException("id required");
        return userDao.deleteById(id);
    }

    public Optional<User> get(Long id) {
        if (id == null) throw new IllegalArgumentException("id required");
        return userDao.findById(id);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name required");
        if (name.length() > 100)
            throw new IllegalArgumentException("name too long");
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("email required");
        if (email.length() > 150)
            throw new IllegalArgumentException("email too long");
        if (!EMAIL_RE.matcher(email).matches())
            throw new IllegalArgumentException("invalid email format");
    }
}