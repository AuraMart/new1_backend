package org.example.Controller;


import lombok.RequiredArgsConstructor;
import org.example.Dto.UserSignUp;
import org.example.Dto.UserSignIn;
import org.example.Response.AuthenticationResponse;
import org.example.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {


    private final UserService userService;


    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> useerSignUp(@RequestBody UserSignUp user) {


        return ResponseEntity.ok(userService.userSignUp(user));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> userSignIn(@RequestBody UserSignIn user) {


        return ResponseEntity.ok(userService.userSignIn(user));

    }
}
