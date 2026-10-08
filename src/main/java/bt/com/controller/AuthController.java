package bt.com.controller;


import bt.com.dto.module.Login;
import bt.com.dto.projection.LoginResponse;
import bt.com.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody Login request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}