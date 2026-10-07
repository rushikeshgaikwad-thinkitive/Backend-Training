package bt.com.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bt.com.dto.request.PatientRequest;
import bt.com.dto.response.PatientResponse;
import bt.com.service.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @RequestBody PatientRequest request) {

        PatientResponse response =
                patientService.createPatient(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {

        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                patientService.getPatientById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @RequestBody PatientRequest request) {

        return ResponseEntity.ok(
                patientService.updatePatient(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<PatientResponse>> getActivePatients() {

        return ResponseEntity.ok(
                patientService.getActivePatients()
        );
    }
}