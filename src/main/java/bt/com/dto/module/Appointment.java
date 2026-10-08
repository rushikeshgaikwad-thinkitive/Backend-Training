package bt.com.dto.module;

import java.time.LocalDate;
import java.time.LocalTime;

import bt.com.dto.constants.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    private Long id;

    @NotNull(message = Messages.APPOINTMENT_PATIENT_REQUIRED)
    private Long patientId;

    @NotNull(message = Messages.APPOINTMENT_DOCTOR_REQUIRED)
    private Long doctorId;

    @NotNull(message = Messages.APPOINTMENT_DATE_REQUIRED)
    private LocalDate appointmentDate;

    @NotNull(message = Messages.APPOINTMENT_TIME_REQUIRED)
    private LocalTime appointmentTime;

    @NotBlank(message = Messages.APPOINTMENT_REASON_REQUIRED)
    @Size(
            max = 500,
            message = Messages.APPOINTMENT_REASON_MAX_LENGTH
    )
    private String reason;

    @Size(
            max = 1000,
            message = Messages.APPOINTMENT_NOTES_MAX_LENGTH
    )
    private String notes;
}