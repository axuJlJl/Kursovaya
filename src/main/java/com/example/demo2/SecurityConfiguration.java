package com.example.demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/edit_question","/edit_survey","new_formQuestion", "new_question", "new_survey").hasRole("ADMIN")
                        .requestMatchers("/index").access(new WebExpressionAuthorizationManager("hasRole('ADMIN') or hasRole('USER')"))
                        .anyRequest().authenticated()

                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);


        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                        .username("user1")
                        .password("1")
                        .roles("USER")
                        .build();
        UserDetails user2 =
                org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                        .username("admin2")
                        .password("2")
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(user,user2);
    }


}