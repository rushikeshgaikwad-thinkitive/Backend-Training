package com.backendTraining;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import bt.com.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mock.web.MockHttpServletRequest;

class AuthenticationErrorHandlerTests {

    @Test
    void invalidCredentialsReturnUnauthorizedInsteadOfInternalServerError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = new MockHttpServletRequest(
                "POST", "/api/auth/login");

        var response = handler.handleAuthenticationFailure(
                new BadCredentialsException("Bad credentials"),
                request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().getStatus());
        assertEquals(
                "Invalid email or password, or this account is not active.",
                response.getBody().getMessage());
    }
}
