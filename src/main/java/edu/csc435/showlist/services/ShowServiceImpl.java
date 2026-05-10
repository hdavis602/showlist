package edu.csc435.showlist.services;

import edu.csc435.showlist.*;
import edu.csc435.showlist.exceptions.*;
import edu.csc435.showlist.db.ShowRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ShowServiceImpl implements ShowService {

    private static final Logger log = LoggerFactory.getLogger(ShowServiceImpl.class);
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
            log.warn("addShow: invalid input (title={}, status={})", title, status);
            throw new BadRequestException("Invalid input.");
        }

        try {
            Show show = new Show(user, title, Status.fromString(status));
            return showRepository.save(show);
        } catch (IllegalArgumentException e) {
            log.warn("addShow: invalid status input: {}", status);
            throw new BadRequestException("Invalid input.");
        }
    }

    @Override
    public Show updateShow(User user, UUID showId, String newStatus, Integer newRating) {
        if (showId == null || (newStatus == null && newRating == null)) {
            log.warn("updateShow: invalid input (showId={}, title={}, status={})", showId, newStatus, newRating);
            throw new BadRequestException("Invalid input.");
        }

        Show show = showRepository.findById(showId).orElseThrow(() -> {
            log.warn("updateShow: showId={} not found", showId);
            return new NotFoundException("Show not found.");
        });

        if (!show.getUser().getUid().equals(user.getUid())) {
            log.warn("updateShow: user mismatch (uid={}, expects {})", user.getUid(), show.getUser().getUid());
            throw new UnauthorizedException("Invalid credentials to access resource.");
        }

        try {
            if (newStatus != null) show.setStatus(newStatus);
            if (newRating != null) show.setRating(newRating);
            return showRepository.save(show);

        } catch (IllegalArgumentException e) {
            log.warn("updateShow: invalid status input: {}", newStatus);
            throw new BadRequestException("Invalid input.");
        }
    }

    @Override
    public void deleteShow(User user, UUID showId) {
        if (showId == null) {
            log.warn("deleteShow: showId is null");
            throw new BadRequestException("Invalid input.");
        }

        Show show = showRepository.findById(showId).orElseThrow(() -> {
            log.warn("deleteShow: showId={} not found", showId);
            return new NotFoundException("Show not found.");
        });

        if (!show.getUser().getUid().equals(user.getUid())) {
            log.warn("deleteShow: user mismatch (uid={}, expects {})", user.getUid(), show.getUser().getUid());
            throw new UnauthorizedException("Invalid credentials to access resource.");
        }

        showRepository.delete(show);
    }

    @Override
    public Show getShow(User user, UUID showId) {
        if (showId == null) {
            log.warn("getShow: showId is null");
            throw new BadRequestException("Invalid input.");
        }

        Show show = showRepository.findById(showId).orElseThrow(() -> {
            log.warn("getShow: showId={} not found", showId);
            return new NotFoundException("Show not found.");
        });

        if (!show.getUser().getUid().equals(user.getUid())) {
            log.warn("getShow: user mismatch (uid={}, expects {})", user.getUid(), show.getUser().getUid());
            throw new UnauthorizedException("Invalid credentials to access resource..");
        }

        return show;
    }
}
