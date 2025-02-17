package fr.doranco.ecom_backend.config;

import fr.doranco.ecom_backend.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.exceptionHandling(e -> e
                .authenticationEntryPoint((request, response, authException) -> {
                    System.out.println("❌ ACCESS DENIED: " + request.getRequestURI());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                })
        );

        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
//                .authorizeHttpRequests(request -> request.requestMatchers("/account/**").permitAll())
//                .authorizeHttpRequests(request -> request.requestMatchers("/categories/**").permitAll())
//                .authorizeHttpRequests(request -> request.requestMatchers("/products/**").permitAll())
//                .authorizeHttpRequests(request -> request.requestMatchers("api/v1/admin/**").hasAuthority("ADMIN"))
//                .authorizeHttpRequests(request -> request.requestMatchers("api/v1/user/**").hasAuthority("USER"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
