package ccb.smonica.recitar_api.config;

import ccb.smonica.recitar_api.filters.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        (requests) -> {
                            requests
                                    .requestMatchers(HttpMethod.POST, "login").permitAll()
                                    .requestMatchers(HttpMethod.POST, "reports/monthly").hasAuthority("ROLE_ADMIN")
                                    .requestMatchers(HttpMethod.GET, "cults/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_AUXILIAR")
                                    .requestMatchers(HttpMethod.POST, "cults/add").hasAnyAuthority("ROLE_ADMIN")
                                    .requestMatchers(HttpMethod.PUT, "cults/update").hasAnyAuthority("ROLE_ADMIN")
                                    .requestMatchers(HttpMethod.GET, "access").hasAuthority("ROLE_ADMIN")
                                    .anyRequest().authenticated();
                        }
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
