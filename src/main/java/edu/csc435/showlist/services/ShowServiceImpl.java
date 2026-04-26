package edu.csc435.showlist.services;

import edu.csc435.showlist.*;
import edu.csc435.showlist.exceptions.*;
import edu.csc435.showlist.db.ShowRepository;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;

    public ShowServiceImpl(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    @Override
    public List<Show> getShows(User user) {
        return showRepository.findByUser(user);
    }

    @Override
    public Show addShow(User user, String title, String status) {
        if (title == null|| status == null) {
            throw new BadRequestException("Invalid input.");
        }

        Show show = new Show(user, title, Status.fromString(status));
        return showRepository.save(show);
    }

    @Override
    public Show updateShow(User user, UUID showId, String status, Integer rating) {
        if (showId == null || (status == null && rating == null)) throw new BadRequestException("Invalid input.");

        Show show = showRepository.findById(showId).orElseThrow(() -> new NotFoundException("Show not found."));

        if (!show.getUser().getUid().equals(user.getUid())) throw new UnauthorizedException("Unauthorized. Please log in.");
        if (status != null) show.setStatus(status);
        if (rating != null) show.setRating(rating);

        return showRepository.save(show);
    }

    @Override
    public void deleteShow(User user, UUID showId) {
        if (showId == null)
            throw new BadRequestException("Missing or invalid show ID.");

        Show show = showRepository.findById(showId).orElseThrow(() -> new NotFoundException("Show not found."));

        if (!show.getUser().getUid().equals(user.getUid()))
            throw new UnauthorizedException("Unauthorized. Please log in.");

        showRepository.delete(show);
    }

    @Override
    public Show getShow(User user, UUID showId) {
        if (showId == null)
            throw new BadRequestException("Missing or invalid show ID.");

        Show show = showRepository.findById(showId).orElseThrow(() -> new NotFoundException("Show not found."));

        if (!show.getUser().getUid().equals(user.getUid()))
            throw new UnauthorizedException("Unauthorized. Please log in.");

        return show;
    }
}
