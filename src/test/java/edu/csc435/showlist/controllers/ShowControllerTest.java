package edu.csc435.showlist.controllers;

import edu.csc435.showlist.Show;
import edu.csc435.showlist.Status;
import edu.csc435.showlist.User;
import edu.csc435.showlist.services.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@WebMvcTest(ShowController.class)
@AutoConfigureMockMvc(addFilters = false)
class ShowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowService showService;

    @MockitoBean
    private UserService userService;

    private OAuth2User principal;
    private User user;

    @BeforeEach
    void setup() {
        principal = mock(OAuth2User.class);
        when(principal.getAttribute("provider")).thenReturn("google");
        when(principal.getName()).thenReturn("123");

        user = new User("google", "123", "test@example.com");

        when(userService.getUser("google", "123")).thenReturn(user);
    }

    //TESTS

    @Test
    void getShows_returnsList() throws Exception {
        List<Show> shows = List.of(
                new Show(user, "Breaking Bad", Status.IN_PROGRESS),
                new Show(user, "Lost", Status.COMPLETED)
        );

        when(showService.getShows(user)).thenReturn(shows);

        mockMvc.perform(get("/showlist/shows")
                        .principal(() -> "123") // required for OAuth2User
                        .requestAttr("org.springframework.security.oauth2.core.user.OAuth2User", principal))
                .andExpect(status().isOk());
    }

    // GET /showlist/shows/{id} TESTS

    @Test
    void getShow_returnsShow() throws Exception {
        UUID id = UUID.randomUUID();
        Show show = new Show(user, "Breaking Bad", Status.IN_PROGRESS);

        when(showService.getShow(user, id)).thenReturn(show);

        mockMvc.perform(get("/showlist/shows/" + id)
                        .principal(() -> "123")
                        .requestAttr("org.springframework.security.oauth2.core.user.OAuth2User", principal))
                .andExpect(status().isOk());
    }

    // POST /showlist/shows/addshow TESTS

    @Test
    void addShow_createsShow() throws Exception {
        UUID id = UUID.randomUUID();
        Show show = Mockito.mock(Show.class);
        when(show.getShowId()).thenReturn(id);

        when(showService.addShow(user, "Breaking Bad", "In Progress")).thenReturn(show);

        mockMvc.perform(post("/showlist/shows/addshow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Breaking Bad\",\"status\":\"WATCHING\"}")
                        .principal(() -> "123")
                        .requestAttr("org.springframework.security.oauth2.core.user.OAuth2User", principal))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.showId").value(id.toString()));
    }

    // PATCH /showlist/shows/{id} TESTS

    @Test
    void updateShow_updatesShow() throws Exception {
        UUID id = UUID.randomUUID();
        Show updated = new Show(user, "Breaking Bad", Status.COMPLETED);

        when(showService.updateShow(user, id, "COMPLETED", null))
                .thenReturn(updated);

        mockMvc.perform(patch("/showlist/shows/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"COMPLETED\"}")
                        .principal(() -> "123")
                        .requestAttr("org.springframework.security.oauth2.core.user.OAuth2User", principal))
                .andExpect(status().isOk());
    }

    // DELETE /showlist/shows/{id} TESTS

    @Test
    void deleteShow_deletesShow() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/showlist/shows/" + id)
                        .principal(() -> "123")
                        .requestAttr("org.springframework.security.oauth2.core.user.OAuth2User", principal))
                .andExpect(status().isNoContent());

        verify(showService).deleteShow(user, id);
    }
}
