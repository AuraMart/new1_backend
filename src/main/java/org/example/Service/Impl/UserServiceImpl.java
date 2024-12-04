package org.example.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.Entity.Role;
import org.example.Entity.UserEntity;
import org.example.Dto.UserSignUp;
import org.example.Dto.UserSignIn;
import org.example.Repository.UserRepository;
import org.example.Response.AuthenticationResponse;
import org.example.Service.JwtService;
import org.example.Service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final org.example.Service.Impl.CustomUserDetailsService customUserDetailsService;

    @Override
    public AuthenticationResponse userSignUp(UserSignUp user) {
        var userEntity = UserEntity.builder()
                .fullName(user.getFullName())
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