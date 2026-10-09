
package bt.com.config;

import bt.com.service.CustomUserDetailsService;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register/patient",
            "/api/auth/register/doctor",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health",
            "/actuator/health/**",
            "/"
    };

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(
                    corsConfigurationSource()))
            .sessionManagement(session -> session
                    .sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/doctors/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/doctors/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/doctors/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/doctors/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/doctors/**")
                    .hasAnyRole("ADMIN", "DOCTOR", "PATIENT")

                .requestMatchers(HttpMethod.POST, "/api/patients/**")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.PUT, "/api/patients/**")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.PATCH, "/api/patients/**")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.DELETE, "/api/patients/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/patients/**")
                    .hasAnyRole("ADMIN", "DOCTOR")

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/patient/**")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/doctor/**")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/date/**")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/scheduled")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments",
                        "/api/appointments/**")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/appointments/**")
                    .hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/appointments/**")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/appointments/*/cancel/patient")
                    .hasAnyRole("ADMIN", "PATIENT")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/appointments/*/cancel/doctor")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/appointments/*/complete")
                    .hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/appointments/**")
                    .hasRole("ADMIN")

                .requestMatchers("/actuator/**")
                    .hasRole("ADMIN")

                .anyRequest().denyAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String role = jwt.getClaimAsString("role");

            if (role == null || role.isBlank()) {
                return List.<GrantedAuthority>of();
            }

            return List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );
        });

        return converter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173"));

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH",
                        "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "Accept"));

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
