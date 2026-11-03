package bt.com.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import bt.com.dto.constants.Literals;
import bt.com.dto.module.Appointment;
import bt.com.dto.module.AppointmentCancellation;
import bt.com.dto.projection.AppointmentView;
import bt.com.service.AppointmentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(Literals.API_APPOINTMENTS)
@Validated
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService) {

        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentView> createAppointment(
            @Valid @RequestBody Appointment appointment,
            Authentication authentication) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        appointmentService.createAppointmentForUser(
                                appointment,
                                authentication.getName(),
                                authentication.getAuthorities().stream()
                                        .filter(authority ->
                                                authority.getAuthority()
                                                        .startsWith("ROLE_"))
                                        .map(authority ->
                                                authority.getAuthority()
                                                        .substring(5))
                                        .findFirst()
                                        .orElse(""))
                );
    }

    @GetMapping
    public ResponseEntity<List<AppointmentView>>
    getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<AppointmentView>> getMyAppointments(
            Authentication authentication) {

        List<AppointmentView> appointments;

        if (hasRole(authentication, "ADMIN")) {
            appointments = appointmentService.getAllAppointments();
        } else if (hasRole(authentication, "PATIENT")) {
            appointments = appointmentService.getMyPatientAppointments(
                    authentication.getName());
        } else if (hasRole(authentication, "DOCTOR")) {
            appointments = appointmentService.getMyDoctorAppointments(
                    authentication.getName());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This account cannot view appointments");
        }

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentView>
    getAppointmentById(
            @PathVariable Long id,
            Authentication authentication) {

        if (hasRole(authentication, "ADMIN")) {
            return ResponseEntity.ok(
                    appointmentService.getAppointmentById(id));
        }
        return ResponseEntity.ok(
                appointmentService.getMyAppointment(
                        id,
                        authentication.getName(),
                        getRole(authentication))
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentView>>
    getAppointmentsByPatient(
            @PathVariable Long patientId,
            Authentication authentication) {

        if (hasRole(authentication, "ADMIN")) {
            return ResponseEntity.ok(
                    appointmentService.getAppointmentsByPatient(patientId));
        }
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatientForDoctor(
                        patientId, authentication.getName())
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentView>>
    getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            Authentication authentication) {

        if (hasRole(authentication, "ADMIN")) {
            return ResponseEntity.ok(
                    appointmentService.getAppointmentsByDoctor(doctorId));
        }
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDoctorForUser(
                        doctorId, authentication.getName())
        );
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentView>>
    getAppointmentsByDate(
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDate(
                        date
                )
        );
    }

    @GetMapping("/scheduled")
    public ResponseEntity<List<AppointmentView>>
    getScheduledAppointments() {

        return ResponseEntity.ok(
                appointmentService.getScheduledAppointments()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentView>
    updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody Appointment appointment,
            Authentication authentication) {

        return ResponseEntity.ok(
                appointmentService.updateAppointmentForUser(
                        id,
                        appointment,
                        authentication.getName(),
                        getRole(authentication))
        );
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentView>
    completeAppointment(
            @PathVariable Long id,
            Authentication authentication) {

        if (!hasRole(authentication, "ADMIN")) {
            return ResponseEntity.ok(
                    appointmentService.completeMyDoctorAppointment(
                            id, authentication.getName()));
        }
        return ResponseEntity.ok(
                appointmentService.completeAppointment(id)
        );
    }

    @PatchMapping("/{id}/cancel/patient")
    public ResponseEntity<AppointmentView>
    cancelByPatient(
            @PathVariable Long id,
            @Valid @RequestBody
            AppointmentCancellation cancellation,
            Authentication authentication) {

        return ResponseEntity.ok(
                appointmentService.cancelMyPatientAppointment(
                        id, cancellation, authentication.getName())
        );
    }

    @PatchMapping("/{id}/cancel/doctor")
    public ResponseEntity<AppointmentView>
    cancelByDoctor(
            @PathVariable Long id,
            @Valid @RequestBody
            AppointmentCancellation cancellation,
            Authentication authentication) {

        return ResponseEntity.ok(
                appointmentService.cancelMyDoctorAppointment(
                        id, cancellation, authentication.getName())
        );
    }

    @PatchMapping("/{id}/cancel/admin")
    public ResponseEntity<AppointmentView> cancelByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentCancellation cancellation) {
        return ResponseEntity.ok(
                appointmentService.cancelAppointmentByAdmin(
                        id, cancellation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.noContent().build();
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        ("ROLE_" + role).equals(authority.getAuthority()));
    }

    private String getRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .filter(authority ->
                        authority.getAuthority().startsWith("ROLE_"))
                .map(authority ->
                        authority.getAuthority().substring(5))
                .findFirst()
                .orElse("");
    }
}