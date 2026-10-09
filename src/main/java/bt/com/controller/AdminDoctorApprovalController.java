package bt.com.controller;
import bt.com.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/doctors")
@RequiredArgsConstructor
public class AdminDoctorApprovalController {

    private final AuthService authService;

    @PatchMapping("/{userId}/approve")
    public ResponseEntity<Void> approveDoctor(
            @PathVariable Long userId) {

        authService.approveDoctor(userId);

        return ResponseEntity.noContent().build();
    }
}
