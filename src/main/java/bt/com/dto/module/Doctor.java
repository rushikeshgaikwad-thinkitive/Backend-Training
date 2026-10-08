package bt.com.dto.module;

import java.time.Instant;

import bt.com.dto.constants.Literals;
import bt.com.dto.constants.Messages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class Doctor {

    private Long id;

    @NotBlank(message = Messages.DOCTOR_NAME_REQUIRED)
    @Size(
            max = 100,
            message = Messages.DOCTOR_NAME_MAX_LENGTH
    )
    private String name;

    @NotBlank(
            message = Messages.DOCTOR_SPECIALIZATION_REQUIRED
    )
    @Size(
            max = 100,
            message = Messages.DOCTOR_SPECIALIZATION_MAX_LENGTH
    )
    private String specialization;

    @NotBlank(
            message = Messages.DOCTOR_PHONE_REQUIRED
    )
    @Pattern(
            regexp = Literals.PHONE_REGEX,
            message = Messages.DOCTOR_PHONE_INVALID
    )
    private String phone;

    @NotBlank(
            message = Messages.DOCTOR_EMAIL_REQUIRED
    )
    @Email(
            message = Messages.DOCTOR_EMAIL_INVALID
    )
    @Size(
            max = 150,
            message = Messages.DOCTOR_EMAIL_MAX_LENGTH
    )
    private String email;

    @NotNull(
            message = Messages.DOCTOR_ACTIVE_REQUIRED
    )
    private Boolean active;

    private Instant createdAt;

    private Instant updatedAt;
}