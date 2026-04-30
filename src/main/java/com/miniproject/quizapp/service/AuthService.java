package com.miniproject.quizapp.service;

import com.miniproject.quizapp.dao.UserDao;
import com.miniproject.quizapp.model.User;

public class AuthService {
    private final UserDao userDao;

    public AuthService() {
        this.userDao = new UserDao();
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and password are required.");
        }
        return userDao.findByUsernameAndPassword(username.trim(), password.trim());
    }
}
