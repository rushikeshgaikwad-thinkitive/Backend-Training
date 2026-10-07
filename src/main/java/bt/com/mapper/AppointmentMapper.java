package bt.com.mapper;

import org.springframework.stereotype.Component;

import bt.com.dto.response.AppointmentResponse;
import bt.com.entity.Appointment;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(
            Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())

                .patientId(
                        appointment.getPatient().getId()
                )

                .patientName(
                        appointment.getPatient().getName()
                )

                .doctorId(
                        appointment.getDoctor().getId()
                )

                .doctorName(
                        appointment.getDoctor().getName()
                )

                .appointmentDate(
                        appointment.getAppointmentDate()
                )

                .appointmentTime(
                        appointment.getAppointmentTime()
                )

                .reason(
                        appointment.getReason()
                )

                .status(
                        appointment.getStatus()
                )

                .notes(
                        appointment.getNotes()
                )

                .cancelledBy(
                        appointment.getCancelledBy()
                )

                .cancellationReason(
                        appointment.getCancellationReason()
                )

                .cancelledAt(
                        appointment.getCancelledAt()
                )

                .build();
    }
}