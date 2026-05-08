package edu.csc435.showlist.services;

import edu.csc435.showlist.User;

public interface UserService {
    User getUser(String provider, String providerId);
    User findOrCreate(String provider, String providerId, String email);
}
