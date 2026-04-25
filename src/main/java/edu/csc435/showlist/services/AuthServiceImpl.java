package edu.csc435.showlist.services;

import edu.csc435.showlist.User;
import edu.csc435.showlist.exceptions.*;
import edu.csc435.showlist.db.UserRepository;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    //placeholder for proper Auth
    private final Map<UUID, UUID> sessions = new LinkedHashMap<>();

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String username, String password_hash) {
        if (username == null || password_hash == null) {
            throw new BadRequestException("Invalid input.");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("Username already exists.");
        }

        User user = new User(username, password_hash);

        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password_hash) {
        if (username == null || password_hash == null) {
            throw new BadRequestException("Invalid input.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password."));

        if (!user.password_hash().equals(password_hash)) throw new UnauthorizedException("Invalid username or password.");

        sessions.put(UUID.randomUUID(), user.uid());

        return user;
    }

    @Override
    public void logout(UUID sessionId) {
        if (!sessions.containsKey(sessionId)) {
            throw new UnauthorizedException("Invalid session.");
        } else sessions.remove(sessionId);
    }

    @Override
    public User getUserFromSession(UUID sessionId) {
        UUID uid = sessions.get(sessionId);
        if (uid == null)
            throw new UnauthorizedException("Unauthorized. Please log in.");

        return userRepository.findById(uid)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized. Please log in."));
    }

}
