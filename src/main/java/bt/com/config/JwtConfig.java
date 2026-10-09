package bt.com.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey jwtSecretKey(
            @Value("${security.jwt.secret}") String secret) {
    	  byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    	  
          if (keyBytes.length < 32) {
              throw new IllegalStateException(
                      "JWT_SECRET must contain at least 32 UTF-8 bytes for HS256");
          }


        return new SecretKeySpec(
                keyBytes,
                "HmacSHA256"
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey secretKey) {

        return new NimbusJwtEncoder(
                new ImmutableSecret<>(
                        secretKey
                )
        );
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey) {

        return org.springframework.security.oauth2.jwt.NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}