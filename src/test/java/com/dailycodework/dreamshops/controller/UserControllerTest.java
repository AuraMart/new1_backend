package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.UserSignIn;
import com.dailycodework.dreamshops.dto.UserSignUp;
import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.response.AuthenticationResponse;
import com.dailycodework.dreamshops.service.user.IUserService;
import com.dailycodework.dreamshops.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp_Success() {
        // Prepare test data
        UserSignUp request = new UserSignUp();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token("sample-token")
                .userId(1L)
                .role(Role.USER)
                .build();

        // Mock the service method
        when(userService.userSignUp(request)).thenReturn(authResponse);

        // Call the controller method
        ResponseEntity<AuthenticationResponse> response = userController.signUp(request);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void testSignUp_AlreadyExists() {
        // Prepare test data
        UserSignUp request = new UserSignUp();
        request.setEmail("john.doe@example.com");

        // Mock the service method to throw AlreadyExistsException
        when(userService.userSignUp(request)).thenThrow(new AlreadyExistsException("User already exists"));

        // Call the controller method
        ResponseEntity<AuthenticationResponse> response = userController.signUp(request);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testSignIn_Success() {
        // Prepare test data
        UserSignIn request = new UserSignIn();
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token("sample-token")
                .userId(1L)
                .role(Role.USER)
                .build();

        // Mock the service method
        when(userService.userSignIn(request)).thenReturn(authResponse);

        // Call the controller method
        ResponseEntity<AuthenticationResponse> response = userController.signIn(request);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void testSignIn_Unauthorized() {
        // Prepare test data
        UserSignIn request = new UserSignIn();
        request.setEmail("john.doe@example.com");
        request.setPassword("wrongpassword");

        // Mock the service method to throw ResponseStatusException
        when(userService.userSignIn(request)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Call the controller method
        ResponseEntity<AuthenticationResponse> response = userController.signIn(request);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testAdminSignUp_Success() {
        // Prepare test data
        UserSignUp request = new UserSignUp();
        request.setFirstName("Admin");
        request.setLastName("User");
        request.setEmail("admin@example.com");
        request.setPassword("adminpassword");

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token("admin-token")
                .userId(1L)
                .role(Role.ADMIN)
                .build();

        // Mock the service method
        when(userService.adminSignUp(request)).thenReturn(authResponse);

        // Call the controller method
        ResponseEntity<AuthenticationResponse> response = userController.adminSignUp(request);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void testAdminSignIn_Success() {
        // Prepare test data
        UserSignIn request = new UserSignIn();
        request.setEmail("admin@example.com");
        request.setPassword("adminpassword");

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token("admin-token")
                .userId(1L)
                .role(Role.ADMIN)
                .build();

        // Mock the service method
        when(userService.adminSignIn(request)).thenReturn(authResponse);

        // Call the controller method
        ResponseEntity<AuthenticationResponse> response = userController.adminSignIn(request);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void testAdminSignIn_Unauthorized() {
        // Prepare test data
        UserSignIn request = new UserSignIn();
        request.setEmail("admin@example.com");
        request.setPassword("wrongpassword");

        // Mock the service method to throw ResponseStatusException
        when(userService.adminSignIn(request)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Call the controller method
        ResponseEntity<AuthenticationResponse> response = userController.adminSignIn(request);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}