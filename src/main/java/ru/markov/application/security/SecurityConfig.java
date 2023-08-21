package ru.markov.application.security;

import ru.markov.application.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/images/*.png").permitAll();
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}brigadmin")
                .roles("USER", "ADMIN")
                .build();
        UserDetails mountBrig1 = User.builder()
                .username("volna1")
                .password("{noop}volna1")
                .roles("USER")
                .build();
        UserDetails mountBrig2 = User.builder()
                .username("volna2")
                .password("{noop}volna2")
                .roles("USER")
                .build();
        UserDetails mountBrig3 = User.builder()
                .username("volna3")
                .password("{noop}volna3")
                .roles("USER")
                .build();
        UserDetails mountBrig4 = User.builder()
                .username("volna4")
                .password("{noop}volna4")
                .roles("USER")
                .build();
        UserDetails buildBrig1 = User.builder()
                .username("sborka1")
                .password("sborka1")
                .roles("USER")
                .build();
        UserDetails buildBrig2 = User.builder()
                .username("sborka2")
                .password("sborka2")
                .roles("USER")
                .build();
        UserDetails buildBrig3 = User.builder()
                .username("sborka3")
                .password("sborka3")
                .roles("USER")
                .build();
        UserDetails buildBrig4 = User.builder()
                .username("sborka4")
                .password("sborka4")
                .roles("USER")
                .build();
        UserDetails techBrig = User.builder()
                .username("tech")
                .password("{noop}tech")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, mountBrig1, mountBrig2 ,mountBrig3 ,mountBrig4 , buildBrig1, buildBrig2, buildBrig3, buildBrig4, techBrig);
    }
}