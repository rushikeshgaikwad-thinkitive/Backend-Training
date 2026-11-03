
package bt.com.config;

import bt.com.service.CustomUserDetailsService;
import bt.com.enums.Permission;
import bt.com.security.RolePermissions;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
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
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CorsConfigurationSource corsConfigurationSource)
            throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(
                    corsConfigurationSource))
            .sessionManagement(session -> session
                    .sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/doctors/me")
                    .hasAuthority(Permission.DOCTOR_VIEW.name())
                .requestMatchers(HttpMethod.PUT, "/api/doctors/me")
                    .hasAuthority(Permission.DOCTOR_UPDATE_OWN.name())
                .requestMatchers(HttpMethod.POST, "/api/doctors/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/doctors/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/doctors/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/doctors/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/doctors/**")
                    .hasAuthority(Permission.DOCTOR_VIEW.name())

                .requestMatchers(HttpMethod.GET, "/api/patients/me")
                    .hasAuthority(Permission.PATIENT_VIEW_OWN.name())
                .requestMatchers(HttpMethod.PUT, "/api/patients/me")
                    .hasAuthority(Permission.PATIENT_UPDATE_OWN.name())
                .requestMatchers(HttpMethod.GET, "/api/patients/assigned")
                    .hasAuthority(Permission.PATIENT_VIEW_ASSIGNED.name())
                .requestMatchers(HttpMethod.GET, "/api/patients/active")
                    .hasAuthority(Permission.PATIENT_MANAGE.name())
                .requestMatchers(HttpMethod.POST, "/api/patients/**")
                    .hasAnyAuthority(
                            Permission.PATIENT_MANAGE.name(),
                            Permission.PATIENT_REGISTER.name())
                .requestMatchers(HttpMethod.PUT, "/api/patients/**")
                    .hasAuthority(Permission.PATIENT_MANAGE.name())
                .requestMatchers(HttpMethod.PATCH, "/api/patients/**")
                    .hasAuthority(Permission.PATIENT_MANAGE.name())
                .requestMatchers(HttpMethod.DELETE, "/api/patients/**")
                    .hasAuthority(Permission.PATIENT_MANAGE.name())
                .requestMatchers(HttpMethod.GET, "/api/patients")
                    .hasAuthority(Permission.PATIENT_MANAGE.name())
                .requestMatchers(HttpMethod.GET, "/api/patients/**")
                    .hasAnyAuthority(
                            Permission.PATIENT_MANAGE.name(),
                            Permission.PATIENT_VIEW_ASSIGNED.name())

                .requestMatchers(
                        HttpMethod.GET, "/api/appointments/me")
                    .hasAnyAuthority(
                            Permission.APPOINTMENT_VIEW_OWN.name(),
                            Permission.APPOINTMENT_VIEW_ASSIGNED.name(),
                            Permission.APPOINTMENT_VIEW_ALL.name())
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/patient/**")
                    .hasAnyAuthority(
                            Permission.APPOINTMENT_VIEW_ALL.name(),
                            Permission.APPOINTMENT_VIEW_ASSIGNED.name())
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/doctor/**")
                    .hasAnyAuthority(
                            Permission.APPOINTMENT_VIEW_ALL.name(),
                            Permission.APPOINTMENT_VIEW_ASSIGNED.name())
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/date/**")
                    .hasAuthority(Permission.APPOINTMENT_VIEW_ALL.name())
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/scheduled")
                    .hasAuthority(Permission.APPOINTMENT_VIEW_ALL.name())
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments")
                    .hasAuthority(Permission.APPOINTMENT_VIEW_ALL.name())
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/appointments/**")
                    .hasAnyAuthority(
                            Permission.APPOINTMENT_VIEW_OWN.name(),
                            Permission.APPOINTMENT_VIEW_ASSIGNED.name(),
                            Permission.APPOINTMENT_VIEW_ALL.name())
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/appointments/**")
                    .hasAuthority(Permission.APPOINTMENT_CREATE.name())
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/appointments/**")
                    .hasAnyAuthority(
                            Permission.APPOINTMENT_UPDATE.name(),
                            Permission.APPOINTMENT_RESCHEDULE.name())
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/appointments/*/cancel/admin")
                    .hasRole("ADMIN")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/appointments/*/cancel/**")
                    .hasAuthority(Permission.APPOINTMENT_CANCEL.name())
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/appointments/*/complete")
                    .hasAuthority(Permission.APPOINTMENT_UPDATE.name())
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

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            RolePermissions.forRole(role).stream()
                    .map(Permission::name)
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
            return authorities;
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
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origin-patterns:http://localhost:5174}")
            String allowedOriginPatterns,
            Environment environment) {

        CorsConfiguration configuration =
                new CorsConfiguration();

        List<String> allowedOrigins = Arrays.stream(
                        allowedOriginPatterns.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();

        if (environment.acceptsProfiles(Profiles.of("prod"))) {
            if (allowedOrigins.stream().anyMatch(origin -> origin.contains("*"))) {
                throw new IllegalStateException(
                        "CORS_ALLOWED_ORIGIN must contain exact origins in production");
            }
            configuration.setAllowedOrigins(allowedOrigins);
        } else {
            configuration.setAllowedOriginPatterns(allowedOrigins);
        }

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
