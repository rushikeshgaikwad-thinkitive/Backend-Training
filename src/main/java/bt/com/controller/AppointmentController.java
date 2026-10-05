package bt.com.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import bt.com.dto.request.AppointmentRequest;
import bt.com.dto.request.CancelAppointmentRequest;
import bt.com.dto.response.AppointmentResponse;
import bt.com.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse createAppointment(
            @RequestBody AppointmentRequest request) {

        return appointmentService.createAppointment(request);
    }


    // GET ALL
    @GetMapping
    public List<AppointmentResponse> getAllAppointments() {

        return appointmentService.getAllAppointments();
    }


    // GET BY ID
    @GetMapping("/{id}")
    public AppointmentResponse getAppointmentById(
            @PathVariable Long id) {

        return appointmentService.getAppointmentById(id);
    }


    // GET BY PATIENT
    @GetMapping("/patient/{patientId}")
    public List<AppointmentResponse> getAppointmentsByPatient(
            @PathVariable Long patientId) {

        return appointmentService.getAppointmentsByPatient(
                patientId
        );
    }


    // GET BY DOCTOR
    @GetMapping("/doctor/{doctorId}")
    public List<AppointmentResponse> getAppointmentsByDoctor(
            @PathVariable Long doctorId) {

        return appointmentService.getAppointmentsByDoctor(
                doctorId
        );
    }


    // GET BY DATE
    @GetMapping("/date/{date}")
    public List<AppointmentResponse> getAppointmentsByDate(
            @PathVariable LocalDate date) {

        return appointmentService.getAppointmentsByDate(
                date
        );
    }


    // GET SCHEDULED
    @GetMapping("/scheduled")
    public List<AppointmentResponse> getScheduledAppointments() {

        return appointmentService.getScheduledAppointments();
    }


    // UPDATE
    @PutMapping("/{id}")
    public AppointmentResponse updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentRequest request) {

        return appointmentService.updateAppointment(
                id,
                request
        );
    }


    // COMPLETE
    @PatchMapping("/{id}/complete")
    public AppointmentResponse completeAppointment(
            @PathVariable Long id) {

        return appointmentService.completeAppointment(id);
    }


    // CANCEL BY PATIENT
    @PatchMapping("/{id}/cancel/patient")
    public AppointmentResponse cancelByPatient(
            @PathVariable Long id,
            @RequestBody CancelAppointmentRequest request) {

        return appointmentService.cancelAppointmentByPatient(
                id,
                request.getCancellationReason()
        );
    }


    // CANCEL BY DOCTOR
    @PatchMapping("/{id}/cancel/doctor")
    public AppointmentResponse cancelByDoctor(
            @PathVariable Long id,
            @RequestBody CancelAppointmentRequest request) {

        return appointmentService.cancelAppointmentByDoctor(
                id,
                request.getCancellationReason()
        );
    }


    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);
    }
}