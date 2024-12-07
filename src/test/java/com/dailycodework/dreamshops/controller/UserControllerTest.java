package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.UserDto;
import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.request.UserUpdateRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.user.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

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
    void testGetUserById_Success() {
        Long userId = 1L;
        User user = new User();
        UserDto userDto = new UserDto();

        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.convertUserToDto(user)).thenReturn(userDto);

        ResponseEntity<ApiResponse> response = userController.getUserById(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(userDto, response.getBody().getData());
    }

    @Test
    void testGetUserById_NotFound() {
        Long userId = 1L;

        when(userService.getUserById(userId)).thenThrow(new ResourceNotFoundException("User not found"));

        ResponseEntity<ApiResponse> response = userController.getUserById(userId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }

    @Test
    void testCreateUser_Success() {
        // Prepare test data
        CreateUserRequest request = new CreateUserRequest();
        User user = new User();  // This is the actual object that will be returned from the controller
        user.setId(1L); // Example ID, you can set other properties as well if needed
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        // Mock the service methods
        when(userService.createUser(request)).thenReturn(user);

        // Call the controller method
        ResponseEntity<ApiResponse> response = userController.createUser(request);

        // Assert the response status and message
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Create User Success!", response.getBody().getMessage());

        // Assert that the response body contains the same user object
        assertEquals(user, response.getBody().getData());  // Compare User objects directly
    }

    @Test
    void testCreateUser_AlreadyExists() {
        CreateUserRequest request = new CreateUserRequest();

        when(userService.createUser(request)).thenThrow(new AlreadyExistsException("User already exists"));

        ResponseEntity<ApiResponse> response = userController.createUser(request);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("User already exists", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }

    @Test
    void testUpdateUser_Success() {
        Long userId = 1L;
        UserUpdateRequest request = new UserUpdateRequest();
        User user = new User();
        UserDto userDto = new UserDto();

        when(userService.updateUser(request, userId)).thenReturn(user);
        when(userService.convertUserToDto(user)).thenReturn(userDto);

        ResponseEntity<ApiResponse> response = userController.updateUser(request, userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Update User Success!", response.getBody().getMessage());
        assertEquals(userDto, response.getBody().getData());
    }

    @Test
    void testUpdateUser_NotFound() {
        Long userId = 1L;
        UserUpdateRequest request = new UserUpdateRequest();

        when(userService.updateUser(request, userId)).thenThrow(new ResourceNotFoundException("User not found"));

        ResponseEntity<ApiResponse> response = userController.updateUser(request, userId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }

    @Test
    void testDeleteUser_Success() {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<ApiResponse> response = userController.deleteUser(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Delete User Success!", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }

    @Test
    void testDeleteUser_NotFound() {
        Long userId = 1L;

        doThrow(new ResourceNotFoundException("User not found")).when(userService).deleteUser(userId);

        ResponseEntity<ApiResponse> response = userController.deleteUser(userId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }
}
