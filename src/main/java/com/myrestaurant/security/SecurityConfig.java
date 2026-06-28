package com.myrestaurant.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/carte", "/menus", "/menus/**",
                                "/reviews", "/reviews/**",
                                "/setup", "/setup/**",
                                "/login",
                                "/placeholder/**", "/uploads/**",
                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/error", "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/admin", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                // Les formulaires publics (setup, reviews) restent proteges par CSRF via Thymeleaf.
                .csrf(csrf -> csrf.ignoringRequestMatchers("/placeholder/**"));

        return http.build();
    }
}
