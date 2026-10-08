package bt.com.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @Valid @RequestBody Appointment appointment) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        appointmentService.createAppointment(
                                appointment
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<AppointmentView>>
    getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentView>
    getAppointmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id)
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentView>>
    getAppointmentsByPatient(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatient(
                        patientId
                )
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentView>>
    getAppointmentsByDoctor(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDoctor(
                        doctorId
                )
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
            @Valid @RequestBody Appointment appointment) {

        return ResponseEntity.ok(
                appointmentService.updateAppointment(
                        id,
                        appointment
                )
        );
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentView>
    completeAppointment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.completeAppointment(id)
        );
    }

    @PatchMapping("/{id}/cancel/patient")
    public ResponseEntity<AppointmentView>
    cancelByPatient(
            @PathVariable Long id,
            @Valid @RequestBody
            AppointmentCancellation cancellation) {

        return ResponseEntity.ok(
                appointmentService.cancelAppointmentByPatient(
                        id,
                        cancellation
                )
        );
    }

    @PatchMapping("/{id}/cancel/doctor")
    public ResponseEntity<AppointmentView>
    cancelByDoctor(
            @PathVariable Long id,
            @Valid @RequestBody
            AppointmentCancellation cancellation) {

        return ResponseEntity.ok(
                appointmentService.cancelAppointmentByDoctor(
                        id,
                        cancellation
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.noContent().build();
    }
}