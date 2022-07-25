package com.ecommerce.application.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationFilter authenticationFilter, AuthenticationVerificationFilter verificationFilter) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .mvcMatchers("/api/user/create")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilter(authenticationFilter)
                .addFilter(verificationFilter)
                .sessionManagement((s) -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager){
        return new AuthenticationFilter(authenticationManager);
    }

    @Bean AuthenticationVerificationFilter authenticationVerificationFilter(AuthenticationManager authenticationManager){
        return new AuthenticationVerificationFilter(authenticationManager);
    }
}
