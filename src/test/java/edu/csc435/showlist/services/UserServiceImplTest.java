package edu.csc435.showlist.services;

import edu.csc435.showlist.User;
import edu.csc435.showlist.db.UserRepository;
import edu.csc435.showlist.exceptions.UnauthorizedException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // getUser() TESTS

    @Test
    void getUser_returnsUser_whenFound() {
        User user = new User("google", "12345", "test@example.com");

        when(userRepository.findByProviderAndProviderId("google", "12345")).thenReturn(Optional.of(user));

        User result = userService.getUser("google", "12345");

        assertEquals("12345", result.getProviderId());
        assertEquals("google", result.getProvider());
        verify(userRepository).findByProviderAndProviderId("google", "12345");
    }

    @Test
    void getUser_throwsUnauthorized_whenNotFound() {
        when(userRepository.findByProviderAndProviderId("google", "12345")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> userService.getUser("google", "12345"));
    }

    // findOrCreate() TESTS

    @Test
    void findOrCreate_returnsExistingUser() {
        User existing = new User("google", "12345", "test@example.com");

        when(userRepository.findByProviderAndProviderId("google", "12345")).thenReturn(Optional.of(existing));

        User result = userService.findOrCreate("google", "12345", "test@example.com");

        assertSame(existing, result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void findOrCreate_createsNewUser_whenNotFound() {
        when(userRepository.findByProviderAndProviderId("google", "12345")).thenReturn(Optional.empty());

        User saved = new User("google", "12345", "test@example.com");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.findOrCreate("google", "12345", "test@example.com");

        assertEquals("12345", result.getProviderId());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }
}
