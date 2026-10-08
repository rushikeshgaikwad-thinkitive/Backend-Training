package bt.com.service;

import java.time.LocalDate;
import java.util.List;

import bt.com.dto.module.Appointment;
import bt.com.dto.module.AppointmentCancellation;
import bt.com.dto.projection.AppointmentView;


public interface AppointmentService {


    AppointmentView createAppointment(
            Appointment appointment
    );

    List<AppointmentView> getAllAppointments();

    AppointmentView getAppointmentById(
            Long id
    );

    AppointmentView updateAppointment(
            Long id,
            Appointment appointment
    );

    void deleteAppointment(
            Long id
    );

    AppointmentView cancelAppointmentByPatient(
            Long appointmentId,
            AppointmentCancellation cancellation
    );

    AppointmentView cancelAppointmentByDoctor(
            Long appointmentId,
            AppointmentCancellation cancellation
    );

    AppointmentView completeAppointment(
            Long appointmentId
    );

    List<AppointmentView> getAppointmentsByPatient(
            Long patientId
    );

    List<AppointmentView> getAppointmentsByDoctor(
            Long doctorId
    );

    List<AppointmentView> getAppointmentsByDate(
            LocalDate date
    );

    List<AppointmentView> getScheduledAppointments();
}