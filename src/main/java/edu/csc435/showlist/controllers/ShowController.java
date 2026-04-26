package edu.csc435.showlist.controllers;

import edu.csc435.showlist.Show;
import edu.csc435.showlist.User;
import edu.csc435.showlist.services.AuthService;
import edu.csc435.showlist.services.ShowService;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/showlist/shows")
public class ShowController {

    private final ShowService showService;
    private final AuthService authService;

    public ShowController(ShowService showService, AuthService authService) {
        this.showService = showService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<?> getShows(@RequestBody Map<String, String> body) {
        UUID uid = UUID.fromString(body.get("uid"));
        User user = authService.getUser(uid);

        List<Show> shows = showService.getShows(user);

        return ResponseEntity.ok(shows);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<?> getShow(@PathVariable UUID showId, @RequestBody Map<String, String> body) {
        UUID uid = UUID.fromString(body.get("uid"));
        User user = authService.getUser(uid);

        Show show = showService.getShow(user, showId);

        return ResponseEntity.ok(show);
    }

    @PostMapping("/addshow")
    public ResponseEntity<?> addShow(@RequestBody Map<String, String> body) {
        UUID uid = UUID.fromString(body.get("uid"));
        String title = body.get("title");
        String status = body.get("status");

        User user = authService.getUser(uid);

        Show show = showService.addShow(user, title, status);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("showId", show.getShowId()));
    }

    @PatchMapping("/{showId}")
    public ResponseEntity<?> updateShow(@PathVariable UUID showId, @RequestBody Map<String, Object> body) {
        UUID uid = UUID.fromString((String) body.get("uid"));

        String status = null;
        if (body.get("status") != null) {
            status = (String) body.get("status");
        }

        Integer rating = null;
        if (body.get("rating") != null) {
            rating = (Integer) body.get("rating");
        }

        User user = authService.getUser(uid);

        Show updated = showService.updateShow(user, showId, status, rating);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{showId}")
    public ResponseEntity<?> deleteShow(@PathVariable UUID showId, @RequestBody Map<String, String> body) {
        UUID uid = UUID.fromString(body.get("uid"));
        User user = authService.getUser(uid);

        showService.deleteShow(user, showId);

        return ResponseEntity.noContent().build();
    }
}
