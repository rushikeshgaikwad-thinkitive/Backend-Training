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

    AppointmentView createAppointmentForUser(
            Appointment appointment,
            String email,
            String role);

    List<AppointmentView> getAllAppointments();

    AppointmentView getAppointmentById(
            Long id
    );

    AppointmentView updateAppointment(
            Long id,
            Appointment appointment
    );

    AppointmentView updateAppointmentForUser(
            Long id,
            Appointment appointment,
            String email,
            String role);

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

    AppointmentView cancelAppointmentByAdmin(
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
    

List<AppointmentView> getMyPatientAppointments(String email);

List<AppointmentView> getMyDoctorAppointments(String email);

List<AppointmentView> getAppointmentsByPatientForDoctor(
        Long patientId,
        String email);

List<AppointmentView> getAppointmentsByDoctorForUser(
        Long doctorId,
        String email);

AppointmentView getMyAppointment(
        Long appointmentId, String email, String role);

AppointmentView cancelMyPatientAppointment(
        Long appointmentId,
        bt.com.dto.module.AppointmentCancellation cancellation,
        String email);

AppointmentView cancelMyDoctorAppointment(
        Long appointmentId,
        bt.com.dto.module.AppointmentCancellation cancellation,
        String email);

AppointmentView completeMyDoctorAppointment(
        Long appointmentId, String email);
}