package edu.csc435.showlist.controllers;

import edu.csc435.showlist.Show;
import edu.csc435.showlist.User;
import edu.csc435.showlist.services.UserService;
import edu.csc435.showlist.services.ShowService;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/showlist/shows")
public class ShowController {

    private final ShowService showService;
    private final UserService userService;

    public ShowController(ShowService showService, UserService userService) {
        this.showService = showService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getShows(@AuthenticationPrincipal OAuth2User principal) {
        User user = resolveUser(principal);
        return ResponseEntity.ok(showService.getShows(user));
    }

    @GetMapping("/{showId}")
    public ResponseEntity<?> getShow(@PathVariable UUID showId, @AuthenticationPrincipal OAuth2User principal) {
        User user = resolveUser(principal);
        Show show = showService.getShow(user, showId);
        return ResponseEntity.ok(show);
    }

    @PostMapping("/addshow")
    public ResponseEntity<?> addShow(@RequestBody Map<String, String> body, @AuthenticationPrincipal OAuth2User principal) {
        String title = body.get("title");
        String status = body.get("status");
        User user = resolveUser(principal);

        Show show = showService.addShow(user, title, status);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("showId", show.getShowId()));
    }

    @PatchMapping("/{showId}")
    public ResponseEntity<?> updateShow(@PathVariable UUID showId, @RequestBody Map<String, Object> body,  @AuthenticationPrincipal OAuth2User principal) {
        String status = null;
        if (body.get("status") != null) {
            status = (String) body.get("status");
        }

        Integer rating = null;
        if (body.get("rating") != null) {
            rating = (Integer) body.get("rating");
        }

        User user = resolveUser(principal);

        Show updated = showService.updateShow(user, showId, status, rating);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{showId}")
    public ResponseEntity<?> deleteShow(@PathVariable UUID showId, @AuthenticationPrincipal OAuth2User principal) {
        User user = resolveUser(principal);
        showService.deleteShow(user, showId);
        return ResponseEntity.noContent().build();
    }

    private User resolveUser(OAuth2User principal) {
        String provider = principal.getAttribute("provider");
        String providerId = principal.getName();
        return userService.getUser(provider, providerId);
    }
}