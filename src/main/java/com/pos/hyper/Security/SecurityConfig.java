package com.pos.hyper.Security;

import com.pos.hyper.model.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public JWTAuthFilter authenticationJwtTokenFilter() {
        return new JWTAuthFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        CorsConfiguration openConfig = new CorsConfiguration();
//        openConfig.setAllowedOrigins(List.of("*"));
//        openConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        openConfig.setAllowedHeaders(List.of("*"));
//        openConfig.setAllowCredentials(false);
//        source.registerCorsConfiguration("/bill.html", openConfig);
//        source.registerCorsConfiguration("/bill/**", openConfig);
//        source.registerCorsConfiguration("/bill.html/**", openConfig);
//
//        return source;
//    }
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    // Default configuration for most endpoints
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    // Option 1: If credentials are NOT needed for bill endpoints
    CorsConfiguration openConfig = new CorsConfiguration();
    openConfig.setAllowedOrigins(List.of("*"));
    openConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    openConfig.setAllowedHeaders(List.of("*"));
    openConfig.setAllowCredentials(false);

    // Option 2: If credentials ARE needed for bill endpoints (uncomment this block and comment out Option 1)
    /*
    CorsConfiguration openConfig = new CorsConfiguration();
    // Specify explicit origins instead of using wildcard
    openConfig.setAllowedOrigins(List.of("http://example.com", "https://example.com", "http://localhost:5173"));
    openConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    openConfig.setAllowedHeaders(List.of("*"));
    openConfig.setAllowCredentials(true);
    */

    // Apply the configuration to bill endpoints
    source.registerCorsConfiguration("/bill.html", openConfig);
    source.registerCorsConfiguration("/bill/**", openConfig);
    source.registerCorsConfiguration("/bill.html/**", openConfig);

    return source;
}




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()) )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/bill.html").permitAll()
                                .requestMatchers("api/bill/**").permitAll()
                                .requestMatchers("/documentation").permitAll()
                                .requestMatchers("/api/image/**").permitAll()
                                .anyRequest().authenticated()
                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }
}
