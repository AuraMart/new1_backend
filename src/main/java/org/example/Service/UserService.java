package org.example.Service;

import org.example.Dto.UserSignIn;
import org.example.Dto.UserSignUp;
import org.example.Response.AuthenticationResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    AuthenticationResponse userSignUp(UserSignUp user);

    AuthenticationResponse userSignIn(UserSignIn user);

    UserDetailsService userDetailsService();
}
