//package com.dailycodework.dreamshops.controller;
//import com.dailycodework.dreamshops.dto.UserDto;
//import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
//import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
//import com.dailycodework.dreamshops.model.User;
//import com.dailycodework.dreamshops.request.CreateUserRequest;
//import com.dailycodework.dreamshops.request.UserUpdateRequest;
//import com.dailycodework.dreamshops.response.ApiResponse;
//import com.dailycodework.dreamshops.service.user.IUserService;
//import com.dailycodework.dreamshops.service.user.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import static org.springframework.http.HttpStatus.CONFLICT;
//import static org.springframework.http.HttpStatus.NOT_FOUND;
//@CrossOrigin("http://localhost:3000")
//@RequiredArgsConstructor
//
//@RestController
//@RequestMapping("${api.prefix}/users")
//public class UserController {
//    @Autowired
//    private final IUserService userService;
//
//    @GetMapping("/{userId}/user")
//    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
//        try {
//            User user = userService.getUserById(userId);
//            UserDto userDto = userService.convertUserToDto(user);
//            return ResponseEntity.ok(new ApiResponse("Success", userDto));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
//        }
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
//        try {
//
//            User user = userService.createUser(request);
////            UserDto userDto = userService.convertUserToDto(user);
//            return ResponseEntity.ok(new ApiResponse("Create User Success!", user));
//        } catch (AlreadyExistsException e) {
//            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
//        }
//    }
//    @PutMapping("/{userId}/update")
//    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
//        try {
//            User user = userService.updateUser(request, userId);
//            UserDto userDto = userService.convertUserToDto(user);
//            return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
//        } catch (ResourceNotFoundException e) {
//           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
//        }
//    }
//    @DeleteMapping("/{userId}/delete")
//    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
//        try {
//            userService.deleteUser(userId);
//            return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
//        }
//    }
//}

package com.dailycodework.dreamshops.controller;


import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import com.dailycodework.dreamshops.dto.UserSignUp;
import com.dailycodework.dreamshops.dto.UserSignIn;
import com.dailycodework.dreamshops.response.AuthenticationResponse;
import com.dailycodework.dreamshops.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {


    private final UserService userService;
    private final UserRepository userRepository;


    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> useerSignUp(@RequestBody UserSignUp user) {


        AuthenticationResponse response = userService.userSignUp(user);

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        return ResponseEntity.ok(new ApiResponse(response.getToken(),existingUser.get().getId()));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse> userSignIn(@RequestBody UserSignIn user) {
//        return ResponseEntity.ok(userService.userSignIn(user));


          Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

          if(existingUser.get().getRole().toString().equals("ADMIN")){

              AuthenticationResponse response = userService.userSignIn(user);
              return ResponseEntity.ok(new ApiResponse(response.getToken(),existingUser.get().getRole()));
          }
          AuthenticationResponse response = userService.userSignIn(user);
          return ResponseEntity.ok(new ApiResponse(response.getToken(),existingUser.get().getId()));

    }

    @PostMapping("/admin/sign-up")
    public ResponseEntity<AuthenticationResponse> adminSignUp(@RequestBody UserSignUp admin) {
        return ResponseEntity.ok(userService.adminSignUp(admin));
    }

    @PostMapping("/admin/sign-in")
    public ResponseEntity<AuthenticationResponse> adminSignIn(@RequestBody UserSignIn admin) {
        return ResponseEntity.ok(userService.adminSignIn(admin));
    }


}
