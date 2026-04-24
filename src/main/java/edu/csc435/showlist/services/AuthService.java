package edu.csc435.showlist.services;

import edu.csc435.showlist.User;

import java.util.UUID;

public interface AuthService {
    User register(String username, String password_hash);
    User login(String username, String password_hash);
    void logout(UUID sessionId);
    User getUserFromSession(UUID sessionId);
}
