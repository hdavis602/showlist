package edu.csc435.showlist.services;

import edu.csc435.showlist.User;
import edu.csc435.showlist.exceptions.*;
import edu.csc435.showlist.db.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId).orElseThrow(() -> {
            log.warn("User not found with these credentials: provider={}, providerId={}", provider, providerId);
            return new UnauthorizedException("Invalid credentials to access resource.");
        });
    }

    @Override
    public User findOrCreate(String provider, String providerId, String email) {
        return userRepository.findByProviderAndProviderId(provider, providerId).orElseGet(() -> {
            log.info("Creating new User provider={}, providerId={}, email={}", provider, providerId, email);
            return userRepository.save(new User(provider, providerId, email));
        });
    }
}
