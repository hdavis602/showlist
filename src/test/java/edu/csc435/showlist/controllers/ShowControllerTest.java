package edu.csc435.showlist.controllers;

import edu.csc435.showlist.Show;
import edu.csc435.showlist.Status;
import edu.csc435.showlist.User;
import edu.csc435.showlist.exceptions.*;
import edu.csc435.showlist.services.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShowControllerTest {

    private ShowService showService;
    private UserService userService;
    private ShowController controller;
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private OAuth2User principal;
    private User user;

    @BeforeEach
    void setup() {
        showService = mock(ShowService.class);
        userService = mock(UserService.class);
        controller = new ShowController(showService, userService);

        principal = mock(OAuth2User.class);
        when(principal.getAttribute("provider")).thenReturn("google");
        when(principal.getName()).thenReturn("123");

        user = new User("google", "123", "test@example.com");
        when(userService.getUser("google", "123")).thenReturn(user);
    }

    //GET showlist/shows TESTS

    @Test
    void getShows_returnsList() {
        List<Show> shows = List.of(
                new Show(user, "Breaking Bad", Status.IN_PROGRESS)
        );

        when(showService.getShows(user)).thenReturn(shows);

        ResponseEntity<?> response = controller.getShows(principal);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(shows, response.getBody());
    }

    @Test
    void getShows_unexpectedError() {
        when(showService.getShows(user)).thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.getShows(principal));

        ResponseEntity<?> response = handler.handleOther(ex);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("An unexpected error occurred.", ((Map<?, ?>) response.getBody()).get("error"));
    }


    //GET showlist/shows/{showid} TESTS

    @Test
    void getShow_returnsShow() {
        UUID id = UUID.randomUUID();
        Show show = new Show(user, "Breaking Bad", Status.IN_PROGRESS);

        when(showService.getShow(user, id)).thenReturn(show);

        ResponseEntity<?> response = controller.getShow(id, principal);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(show, response.getBody());
    }

    @Test
    void getShow_notFound() {
        UUID id = UUID.randomUUID();

        when(showService.getShow(user, id))
                .thenThrow(new NotFoundException("Show not found"));

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> controller.getShow(id, principal));

        ResponseEntity<?> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Show not found", ((Map<?, ?>) response.getBody()).get("error"));
    }


    //POST showlist/shows/addshow TESTS

    @Test
    void addShow_createsShow() {
        UUID id = UUID.randomUUID();
        Show show = mock(Show.class);
        when(show.getShowId()).thenReturn(id);

        when(showService.addShow(user, "Breaking Bad", "In Progress"))
                .thenReturn(show);

        Map<String, String> body = Map.of(
                "title", "Breaking Bad",
                "status", "In Progress"
        );

        ResponseEntity<?> response = controller.addShow(body, principal);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(id, ((Map<?, ?>) response.getBody()).get("showId"));
    }

    @Test
    void addShow_invalidStatus() {
        Map<String, String> body = Map.of(
                "title", "Breaking Bad",
                "status", "INVALID"
        );

        when(showService.addShow(user, "Breaking Bad", "INVALID"))
                .thenThrow(new BadRequestException("Invalid status"));


        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> controller.addShow(body, principal));

        ResponseEntity<?> response = handler.handleBadRequest(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid status", ((Map<?, ?>) response.getBody()).get("error"));
    }


    //PATCH showlist/shows/{showid} TESTS

    @Test
    void updateShow_updatesShow() {
        UUID id = UUID.randomUUID();
        Show updated = new Show(user, "Breaking Bad", Status.COMPLETED);

        when(showService.updateShow(user, id, "Completed", null))
                .thenReturn(updated);

        Map<String, Object> body = Map.of("status", "Completed");

        ResponseEntity<?> response = controller.updateShow(id, body, principal);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(updated, response.getBody());
    }

    @Test
    void updateShow_unauthorized() {
        UUID id = UUID.randomUUID();
        Map<String, Object> body = Map.of("status", "COMPLETED");

        when(showService.updateShow(user, id, "COMPLETED", null))
                .thenThrow(new UnauthorizedException("Invalid credentials to access resource."));


        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> controller.updateShow(id, body, principal));

        ResponseEntity<?> response = handler.handleUnauthorized(ex);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid credentials to access resource.", ((Map<?, ?>) response.getBody()).get("error"));
    }


    //DELETE showlist/shows/{showid} TESTS

    @Test
    void deleteShow_deletesShow() {
        UUID id = UUID.randomUUID();

        ResponseEntity<?> response = controller.deleteShow(id, principal);

        assertEquals(204, response.getStatusCode().value());
        verify(showService).deleteShow(user, id);
    }

    @Test
    void deleteShow_notFound() {
        UUID id = UUID.randomUUID();

        doThrow(new NotFoundException("Show not found"))
                .when(showService).deleteShow(user, id);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> controller.deleteShow(id, principal));

        ResponseEntity<?> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Show not found", ((Map<?, ?>) response.getBody()).get("error"));
    }

}
