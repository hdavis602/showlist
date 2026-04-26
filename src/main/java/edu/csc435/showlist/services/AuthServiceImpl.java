package edu.csc435.showlist.services;

import edu.csc435.showlist.User;
import edu.csc435.showlist.exceptions.*;
import edu.csc435.showlist.db.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String password) {
        if (username == null || password == null) {
            throw new BadRequestException("Invalid input.");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("Username already exists.");
        }

        User user = new User(username, passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            throw new BadRequestException("Invalid input.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password."));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid username or password.");
        }

        return user;
    }

//    @Override
//    public void logout() {}

    @Override
    public User getUser(UUID uid) {
        return userRepository.findById(uid).orElseThrow(() -> new UnauthorizedException("Invalid user id."));
    }
}
