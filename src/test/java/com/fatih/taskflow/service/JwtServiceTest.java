package com.fatih.taskflow.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET =
            "dGVzdC1vbmx5LXNlY3JldC1rZXktZm9yLXVuaXQtdGVzdHMtbm90LWZvci1wcm9kdWN0aW9uLXVzZS1hdC1hbGwtMTIzNDU2Nzg5MA==";

    private final JwtService jwtService = new JwtService(SECRET, 60);

    @Test
    void generateToken_shouldProduceValidToken() {
        String token = jwtService.generateToken(1L, "fatih@example.com");

        System.out.println("URETILEN TOKEN: " + token);

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
        assertThat(jwtService.isTokenValid(token)).isTrue();
        assertThat(jwtService.extractUserId(token)).isEqualTo(1L);
        assertThat(jwtService.extractEmail(token)).isEqualTo("fatih@example.com");
    }

    @Test
    void isTokenValid_shouldReturnFalseWhenSignatureIsTampered() {
        String token = jwtService.generateToken(1L, "fatih@example.com");

        String tampered = token.substring(0, token.length() - 4) + "AAAA";

        assertThat(jwtService.isTokenValid(tampered)).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalseWhenTokenIsGarbage() {
        assertThat(jwtService.isTokenValid("bu-bir-token-degil")).isFalse();
    }
}
