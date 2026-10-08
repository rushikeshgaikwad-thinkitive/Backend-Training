package bt.com.dto.projection;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import bt.com.enums.AppointmentStatus;
import bt.com.enums.CancelledBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AppointmentView {

    private Long id;

    private Long patientId;

    private String patientName;

    private Long doctorId;

    private String doctorName;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String reason;

    private AppointmentStatus status;

    private String notes;

    private CancelledBy cancelledBy;

    private String cancellationReason;

    private Instant cancelledAt;

    private Instant createdAt;

    private Instant updatedAt;
}