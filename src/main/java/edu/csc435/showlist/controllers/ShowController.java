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

    @PostMapping("/getshows")
    public ResponseEntity<?> getShows(@RequestBody Map<String, String> body) {
        UUID uid = UUID.fromString(body.get("uid"));
        User user = authService.getUser(uid);

        List<Show> shows = showService.getShows(user);

        return ResponseEntity.ok(shows);
    }

    @PostMapping("/getshow/{showId}")
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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("showId", show.getShowId()));
    }

    @PutMapping("/updateshow/{showId}")
    public ResponseEntity<?> updateShow(@PathVariable UUID showId, @RequestBody Map<String, Object> body) {

        UUID uid = UUID.fromString((String) body.get("uid"));
        String status = (String) body.get("status");
        Integer rating = (body.get("rating") != null)
                ? (Integer) body.get("rating")
                : null;

        User user = authService.getUser(uid);

        Show updated = showService.updateShow(user, showId, status, rating);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deleteshow/{showId}")
    public ResponseEntity<?> deleteShow(@PathVariable UUID showId, @RequestBody Map<String, String> body) {

        UUID uid = UUID.fromString(body.get("uid"));
        User user = authService.getUser(uid);

        showService.deleteShow(user, showId);

        return ResponseEntity.noContent().build();
    }
}
