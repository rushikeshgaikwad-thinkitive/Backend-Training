package bt.com.controller;

import bt.com.dto.module.DoctorProfileUpdate;
import bt.com.dto.projection.DoctorView;
import bt.com.service.DoctorService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final DoctorService doctorService;

    @GetMapping("/api/doctors/me")
    public ResponseEntity<DoctorView> getMyDoctorProfile(
            Authentication authentication) {
        return ResponseEntity.ok(
                doctorService.getMyDoctorProfile(authentication.getName()));
    }

    @PutMapping("/api/doctors/me")
    public ResponseEntity<DoctorView> updateMyDoctorProfile(
            Authentication authentication,
            @Valid @RequestBody DoctorProfileUpdate update) {
        return ResponseEntity.ok(
                doctorService.updateMyDoctorProfile(
                        authentication.getName(), update));
    }
}
