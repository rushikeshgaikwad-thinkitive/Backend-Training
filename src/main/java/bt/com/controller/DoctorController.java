package bt.com.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bt.com.dto.constants.Literals;
import bt.com.dto.module.Doctor;
import bt.com.dto.projection.DoctorView;
import bt.com.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(Literals.API_DOCTORS)
@Validated
@Tag(
        name = "Doctors",
        description = "Doctor management APIs"
)
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(
            DoctorService doctorService) {

        this.doctorService = doctorService;
    }

    @Operation(
            summary = "Create doctor",
            description = "Creates a new doctor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Doctor created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid doctor data"
            )
    })
    @PostMapping
    public ResponseEntity<DoctorView> createDoctor(
            @Valid @RequestBody Doctor doctor) {

        DoctorView response =
                doctorService.createDoctor(doctor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<DoctorView>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorView> getDoctorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorView> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody Doctor doctor) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(
                        id,
                        doctor
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<DoctorView>> getActiveDoctors() {

        return ResponseEntity.ok(
                doctorService.getActiveDoctors()
        );
    }
}