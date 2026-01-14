package com.university.parking.user.repository;

import com.university.parking.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public UserRepository() {
        // Usuario inicial (admin)
        users.put(
            "admin@university.edu",
            new User("admin@university.edu", "Administrator", "ADMIN")
        );
    }

    public User save(User user) {
        users.put(user.getEmail(), user);
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
