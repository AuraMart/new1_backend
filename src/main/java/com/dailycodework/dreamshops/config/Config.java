package com.dailycodework.dreamshops.config;

import lombok.RequiredArgsConstructor;
import com.dailycodework.dreamshops.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@RequiredArgsConstructor
public class Config {

    private final UserRepository userRepository;
    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
