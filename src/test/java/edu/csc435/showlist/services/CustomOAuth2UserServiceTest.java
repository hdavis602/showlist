package edu.csc435.showlist.services;

import edu.csc435.showlist.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private OAuth2UserRequest userRequest;

    @Mock
    private OAuth2User oAuth2User;

    private ClientRegistration clientRegistration;

    @InjectMocks
    private CustomOAuth2UserService customService;

    @BeforeEach
    void setup() {
        // Build a real ClientRegistration
        clientRegistration = ClientRegistration
                .withRegistrationId("google")
                .clientId("test-client")
                .clientSecret("test-secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost")
                .scope("email", "profile")
                .authorizationUri("http://auth")
                .tokenUri("http://token")
                .userInfoUri("http://userinfo")
                .userNameAttributeName("sub")
                .clientName("Google")
                .build();

        when(userRequest.getClientRegistration()).thenReturn(clientRegistration);

        // Provide a real access token
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "fake-token-value",
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );
    }


    @Test
    void loadUser_callsFindOrCreate_andReturnsOAuth2User() {

        // Mock OAuth2 attributes
        when(oAuth2User.getAttribute("sub")).thenReturn("12345");
        when(oAuth2User.getAttribute("email")).thenReturn("test@example.com");

        // Spy on the service so we can override super.loadUser()
        CustomOAuth2UserService spyService = Mockito.spy(customService);

        // Make super.loadUser(req) return our mocked OAuth2User
        doReturn(oAuth2User).when(spyService).loadUserFromProvider(userRequest);

        // Mock userService behavior
        when(userService.findOrCreate("google", "12345", "test@example.com"))
                .thenReturn(new User("google", "12345", "test@example.com"));

        // Execute
        OAuth2User result = spyService.loadUser(userRequest);

        // Verify behavior
        verify(userService).findOrCreate("google", "12345", "test@example.com");
        assertSame(oAuth2User, result);
    }
}
