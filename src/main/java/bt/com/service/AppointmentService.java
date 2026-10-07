package bt.com.service;

import java.time.LocalDate;
import java.util.List;

import bt.com.dto.request.AppointmentRequest;
import bt.com.dto.response.AppointmentResponse;

public interface AppointmentService {

    AppointmentResponse createAppointment(
            AppointmentRequest request
    );

    List<AppointmentResponse> getAllAppointments();

    AppointmentResponse getAppointmentById(
            Long id
    );

    AppointmentResponse updateAppointment(
            Long id,
            AppointmentRequest request
    );

    void deleteAppointment(
            Long id
    );

    AppointmentResponse cancelAppointmentByPatient(
            Long appointmentId,
            String reason
    );

    AppointmentResponse cancelAppointmentByDoctor(
            Long appointmentId,
            String reason
    );

    AppointmentResponse completeAppointment(
            Long appointmentId
    );

    List<AppointmentResponse> getAppointmentsByPatient(
            Long patientId
    );

    List<AppointmentResponse> getAppointmentsByDoctor(
            Long doctorId
    );

    List<AppointmentResponse> getAppointmentsByDate(
            LocalDate date
    );

    List<AppointmentResponse> getScheduledAppointments();
}