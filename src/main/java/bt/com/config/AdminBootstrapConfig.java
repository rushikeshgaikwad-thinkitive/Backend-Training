package bt.com.config;


import java.util.Locale;

import bt.com.entity.UserEntity;
import bt.com.enums.Role;
import bt.com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

@Configuration
public class AdminBootstrapConfig {

    @Bean
    CommandLineRunner bootstrapAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${security.bootstrap-admin.email:}") String adminEmail,
            @Value("${security.bootstrap-admin.password:}") String adminPassword) {

        return args -> {
            if (!StringUtils.hasText(adminEmail)
                    || !StringUtils.hasText(adminPassword)) {
                return;
            }

            if (adminPassword.length() < 12) {
                throw new IllegalStateException(
                        "BOOTSTRAP_ADMIN_PASSWORD must be at least 12 characters");
            }

            String normalizedEmail =
                    adminEmail.trim().toLowerCase(Locale.ROOT);

            if (userRepository.existsByEmail(normalizedEmail)) {
                return;
            }

            UserEntity admin = UserEntity.builder()
                    .email(normalizedEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .active(true)
                    .build();

            userRepository.save(admin);
        };
    }
}
