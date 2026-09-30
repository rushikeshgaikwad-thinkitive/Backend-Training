package bt.com.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bt.com.dto.request.DoctorRequest;
import bt.com.dto.response.DoctorResponse;
import bt.com.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Create Doctor
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @RequestBody DoctorRequest request) {

        DoctorResponse response =
                doctorService.createDoctor(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all Doctors
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }

    // Get Doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(id)
        );
    }

    // Update Doctor
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorRequest request) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(id, request)
        );
    }

    // Delete Doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }

    // Get Active Doctors
    @GetMapping("/active")
    public ResponseEntity<List<DoctorResponse>> getActiveDoctors() {

        return ResponseEntity.ok(
                doctorService.getActiveDoctors()
        );
    }
}