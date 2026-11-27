package kr.swdl.sprint2.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth ->
                auth.requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/user/**").authenticated()
                    .anyRequest().permitAll());

        http.formLogin(form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("ID")
                    .failureUrl("/login?error=true"));

        return http.build();
    }

}
