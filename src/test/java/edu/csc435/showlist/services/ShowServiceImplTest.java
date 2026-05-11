package edu.csc435.showlist.services;

import edu.csc435.showlist.Show;
import edu.csc435.showlist.Status;
import edu.csc435.showlist.User;
import edu.csc435.showlist.exceptions.*;
import edu.csc435.showlist.db.ShowRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ShowServiceImplTest {

    @Mock
    private ShowRepository showRepository;

    @InjectMocks
    private ShowServiceImpl showService;

    private User user;
    private User otherUser;

    @BeforeEach
    void setup() {
        user = new User("google", "123", "test@example.com");
        user.setUid(UUID.randomUUID());
        otherUser = new User("google", "999", "other@example.com");
        otherUser.setUid(UUID.randomUUID());
    }

    // addShow() TESTS

    @Test
    void addShow_createsNewShow() {
        Show saved = new Show(user, "Scavenger's Reign", Status.IN_PROGRESS);
        when(showRepository.save(any())).thenReturn(saved);

        Show result = showService.addShow(user, "Scavenger's Reign", "In Progress");

        assertEquals("Scavenger's Reign", result.getTitle());
        assertEquals(Status.IN_PROGRESS, result.getStatus());
        verify(showRepository).save(any());
    }

    @Test
    void addShow_nullTitle_throws() {
        assertThrows(BadRequestException.class, () ->
                showService.addShow(user, null, "In Progress")
        );
    }

    @Test
    void addShow_invalidStatus_throws() {
        assertThrows(BadRequestException.class, () ->
                showService.addShow(user, "Scavenger's Reign", "INVALID")
        );
    }

    // getShows() TESTS

    @Test
    void getShows_returnsList() {
        List<Show> shows = List.of(
                new Show(user, "ATitle", Status.IN_PROGRESS),
                new Show(user, "BTitle", Status.COMPLETED)
        );

        when(showRepository.findByUser(user)).thenReturn(shows);

        List<Show> result = showService.getShows(user);

        assertEquals(2, result.size());
        verify(showRepository).findByUser(user);
    }

    // updateShow() TESTS
    @Test
    void updateShow_updatesShow() {
        UUID id = UUID.randomUUID();
        Show existing = new Show(user, "Title", Status.IN_PROGRESS);

        when(showRepository.findById(id)).thenReturn(Optional.of(existing));
        when(showRepository.save(any())).thenReturn(existing);

        Show result = showService.updateShow(user, id, "Completed", 10);

        assertEquals("Title", result.getTitle());
        assertEquals(Status.COMPLETED, result.getStatus());
        verify(showRepository).save(existing);
    }

    @Test
    void updateShow_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(showRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                showService.updateShow(user, id, "In Progress", null)
        );
    }

    @Test
    void updateShow_wrongUser_throws() {
        UUID id = UUID.randomUUID();
        Show existing = new Show(otherUser, "Title", Status.IN_PROGRESS);

        when(showRepository.findById(id)).thenReturn(Optional.of(existing));

        assertThrows(UnauthorizedException.class, () ->
                showService.updateShow(user, id, "In Progress", null)
        );
    }

    @Test
    void updateShow_invalidStatus_throws() {
        UUID id = UUID.randomUUID();
        Show existing = new Show(user, "Title", Status.IN_PROGRESS);

        when(showRepository.findById(id)).thenReturn(Optional.of(existing));

        assertThrows(BadRequestException.class, () -> showService.updateShow(user, id, "INVALID", null));
    }

    // deleteShow() TESTS

    @Test
    void deleteShow_deletesShow() {
        UUID id = UUID.randomUUID();
        Show existing = new Show(user, "Title", Status.IN_PROGRESS);

        when(showRepository.findById(id)).thenReturn(Optional.of(existing));

        showService.deleteShow(user, id);

        verify(showRepository).delete(existing);
    }

    @Test
    void deleteShow_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(showRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                showService.deleteShow(user, id)
        );
    }

    @Test
    void deleteShow_wrongUser_throws() {
        UUID id = UUID.randomUUID();
        Show existing = new Show(otherUser, "Title", Status.IN_PROGRESS);

        when(showRepository.findById(id)).thenReturn(Optional.of(existing));

        assertThrows(UnauthorizedException.class, () ->
                showService.deleteShow(user, id)
        );
    }
}
