package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.model.Role;
import lombok.RequiredArgsConstructor;
import com.dailycodework.dreamshops.dto.UserSignIn;
import com.dailycodework.dreamshops.dto.UserSignUp;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.response.AuthenticationResponse;
import com.dailycodework.dreamshops.service.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final com.dailycodework.dreamshops.service.CustomUserDetailsService customUserDetailsService;

    @Override
    public AuthenticationResponse userSignUp(UserSignUp user) {
        var userEntity = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(userEntity);

        var jwt = jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public AuthenticationResponse userSignIn(UserSignIn user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()));

        var userEntity = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        var jwt = jwtService.generateToken(userEntity);
        return AuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

}