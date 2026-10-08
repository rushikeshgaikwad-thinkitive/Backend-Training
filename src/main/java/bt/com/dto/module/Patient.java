package bt.com.dto.module;

import java.time.Instant;

import bt.com.dto.constants.Literals;
import bt.com.dto.constants.Messages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class Patient {

    private Long id;

    @NotBlank(message = Messages.PATIENT_NAME_REQUIRED)
    @Size(
            max = 100,
            message = Messages.PATIENT_NAME_MAX_LENGTH
    )
    private String name;

    @NotNull(message = Messages.PATIENT_AGE_REQUIRED)
    @Min(
            value = 0,
            message = Messages.PATIENT_AGE_NEGATIVE
    )
    @Max(
            value = 150,
            message = Messages.PATIENT_AGE_MAX
    )
    private Integer age;

    @NotBlank(message = Messages.PATIENT_GENDER_REQUIRED)
    @Size(
            max = 20,
            message = Messages.PATIENT_GENDER_MAX_LENGTH
    )
    private String gender;

    @NotBlank(message = Messages.PATIENT_PHONE_REQUIRED)
    @Pattern(
            regexp = Literals.PHONE_REGEX,
            message = Messages.PATIENT_PHONE_INVALID
    )
    private String phone;

    @NotBlank(message = Messages.PATIENT_EMAIL_REQUIRED)
    @Email(message = Messages.PATIENT_EMAIL_INVALID)
    @Size(
            max = 150,
            message = Messages.PATIENT_EMAIL_MAX_LENGTH
    )
    private String email;

    @NotNull(message = Messages.PATIENT_ACTIVE_REQUIRED)
    private Boolean active;

    private Instant createdAt;

    private Instant updatedAt;
}