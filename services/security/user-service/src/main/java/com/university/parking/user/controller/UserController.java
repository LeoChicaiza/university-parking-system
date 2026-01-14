package com.university.parking.user.controller;

import com.university.parking.user.model.User;
import com.university.parking.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User create(@RequestBody Map<String, String> body) {
        return service.createUser(
                body.get("email"),
                body.get("name"),
                body.get("role")
        );
    }

    @GetMapping("/{email}")
    public User get(@PathVariable String email) {
        return service.getUser(email);
    }

    @GetMapping("/{email}/role")
    public String role(@PathVariable String email) {
        return service.getUserRole(email);
    }

    @GetMapping("/{email}/active")
    public boolean active(@PathVariable String email) {
        return service.isUserActive(email);
    }

    @PostMapping("/{email}/deactivate")
    public void deactivate(@PathVariable String email) {
        service.deactivateUser(email);
    }
}
