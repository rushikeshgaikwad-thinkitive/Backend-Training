package bt.com.validator;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import bt.com.dto.request.AppointmentRequest;

@Component
public class AppointmentValidator {

    public void validate(AppointmentRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Appointment request cannot be null"
            );
        }

        if (request.getPatientId() == null) {
            throw new IllegalArgumentException(
                    "Patient ID is required"
            );
        }

        if (request.getDoctorId() == null) {
            throw new IllegalArgumentException(
                    "Doctor ID is required"
            );
        }

        if (request.getAppointmentDate() == null) {
            throw new IllegalArgumentException(
                    "Appointment date is required"
            );
        }

        if (request.getAppointmentTime() == null) {
            throw new IllegalArgumentException(
                    "Appointment time is required"
            );
        }

        if (request.getReason() == null
                || request.getReason().isBlank()) {

            throw new IllegalArgumentException(
                    "Appointment reason is required"
            );
        }

        validateAppointmentDateTime(
                request.getAppointmentDate(),
                request.getAppointmentTime()
        );
    }


    public void validateAppointmentDateTime(
            LocalDate appointmentDate,
            LocalTime appointmentTime) {

        if (appointmentDate.isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Appointment date cannot be in the past"
            );
        }

        if (appointmentDate.equals(LocalDate.now())
                && appointmentTime.isBefore(LocalTime.now())) {

            throw new IllegalArgumentException(
                    "Appointment time cannot be in the past"
            );
        }
    }
}