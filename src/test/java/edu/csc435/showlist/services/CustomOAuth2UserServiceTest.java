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
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @InjectMocks
    private CustomOAuth2UserService customService;

    @BeforeEach
    void setup() { //BAD STUPID BAD
        //build a real ClientRegistration for testing
        ClientRegistration clientRegistration = ClientRegistration
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
    }


    @Test
    void loadUser_callsFindOrCreate_andReturnsOAuth2User() {

        //mock OAuth2 attributes
        when(oAuth2User.getAttribute("sub")).thenReturn("12345");
        when(oAuth2User.getAttribute("email")).thenReturn("test@example.com");

        //a mockito spy of the auth service
        CustomOAuth2UserService theSpyer = Mockito.spy(customService);
        doReturn(oAuth2User).when(theSpyer).loadUserFromProvider(userRequest);

        //mock the mocked user
        when(userService.findOrCreate("google", "12345", "test@example.com")).thenReturn(new User("google", "12345", "test@example.com"));

        //gobble up that registration request
        OAuth2User result = theSpyer.loadUser(userRequest);

        verify(userService).findOrCreate("google", "12345", "test@example.com");
        assertSame(oAuth2User, result);
    }
}
