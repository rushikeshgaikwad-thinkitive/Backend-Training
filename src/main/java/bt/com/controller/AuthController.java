
package bt.com.controller;

import bt.com.dto.module.DoctorRegistrationRequest;
import bt.com.dto.module.Login;
import bt.com.dto.module.PatientRegistrationRequest;
import bt.com.dto.projection.LoginResponse;
import bt.com.dto.projection.Registration;
import bt.com.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
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

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<Registration> registerPatient(
            @Valid @RequestBody PatientRegistrationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerPatient(request));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<Registration> registerDoctor(
            @Valid @RequestBody DoctorRegistrationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerDoctor(request));
    }
}
