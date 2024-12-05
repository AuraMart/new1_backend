package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.dto.UserSignIn;
import com.dailycodework.dreamshops.dto.UserSignUp;
import com.dailycodework.dreamshops.response.AuthenticationResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    AuthenticationResponse userSignUp(UserSignUp user);

    AuthenticationResponse userSignIn(UserSignIn user);

    UserDetailsService userDetailsService();

    AuthenticationResponse adminSignIn(UserSignIn admin);

    AuthenticationResponse adminSignUp(UserSignUp admin);
}
