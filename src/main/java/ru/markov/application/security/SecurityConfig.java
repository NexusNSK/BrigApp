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
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}user")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}brigadmin")
                .roles("USER", "ADMIN")
                .build();
        UserDetails mountBrig = User.builder()
                .username("volna")
                .password("{noop}volnabrig")
                .roles("USER", "ADMIN")
                .build();
        UserDetails buildBrig = User.builder()
                .username("sborka")
                .password("sborkabrig")
                .roles("USER", "ADMIN")
                .build();
        UserDetails techBrig = User.builder()
                .username("tech")
                .password("{noop}techbrig")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin, mountBrig, buildBrig, techBrig);
    }
}