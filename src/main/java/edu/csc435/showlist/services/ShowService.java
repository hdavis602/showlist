package edu.csc435.showlist.services;

import edu.csc435.showlist.Show;
import edu.csc435.showlist.User;

import java.util.*;

public interface ShowService {
    List<Show> getShows(User user);
    Show addShow(User user, String title, String status);
    Show updateShow(User user, UUID showId, String status, Integer rating);
    void deleteShow(User user, UUID showId);
    Show getShow(User user, UUID showId);
}
