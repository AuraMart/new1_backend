package com.dailycodework.dreamshops.controller;
import com.dailycodework.dreamshops.dto.UserSignIn;
import com.dailycodework.dreamshops.dto.UserSignUp;
import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.response.AuthenticationResponse;
import com.dailycodework.dreamshops.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    @Autowired
    private final IUserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody UserSignUp request) {
        System.out.println(request);
        try {
            return ResponseEntity.ok(userService.userSignUp(request));
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody UserSignIn request) {
        try {
            return ResponseEntity.ok(userService.userSignIn(request));
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/admin/signup")
    public ResponseEntity<AuthenticationResponse> adminSignUp(@RequestBody UserSignUp request) {
        return ResponseEntity.ok(userService.adminSignUp(request));
    }


    @PostMapping("/admin/signin")
    public ResponseEntity<AuthenticationResponse> adminSignIn(@RequestBody UserSignIn request) {
        try {
            return ResponseEntity.ok(userService.adminSignIn(request));
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
