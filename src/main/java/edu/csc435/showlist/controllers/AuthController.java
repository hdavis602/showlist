package edu.csc435.showlist.controllers;

import edu.csc435.showlist.User;
import edu.csc435.showlist.services.AuthService;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping(value="showlist/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = authService.register(username, password);

        return ResponseEntity.ok(Map.of("uid", user.getUid()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = authService.login(username, password);

        return ResponseEntity.ok(Map.of("userId", user.getUid()));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout() {
        throw new  UnsupportedOperationException("Not supported yet.");
    }
}