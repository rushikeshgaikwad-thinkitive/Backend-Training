package bt.com.controller;



import bt.com.entity.DoctorEntity;
import bt.com.entity.PatientEntity;
import bt.com.repository.DoctorRepository;
import bt.com.repository.PatientRepository;
import bt.com.service.DoctorService;
import bt.com.service.PatientService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping("/api/patients/me")
    public ResponseEntity<?> getMyPatientProfile(
            Authentication authentication) {

        PatientEntity patient = patientRepository
                .findByUser_EmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient profile not found for this account"));

        return ResponseEntity.ok(
                patientService.getPatientById(patient.getId()));
    }

    @GetMapping("/api/doctors/me")
    public ResponseEntity<?> getMyDoctorProfile(
            Authentication authentication) {

        DoctorEntity doctor = doctorRepository
                .findByUser_EmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Doctor profile not found for this account"));

        return ResponseEntity.ok(
                doctorService.getDoctorById(doctor.getId()));
    }
}
