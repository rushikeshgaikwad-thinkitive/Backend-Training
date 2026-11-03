package bt.com.dto.module;

import bt.com.dto.constants.Literals;
import bt.com.dto.constants.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PatientProfileUpdate(
        @NotBlank(message = Messages.PATIENT_PHONE_REQUIRED)
        @Pattern(
                regexp = Literals.PHONE_REGEX,
                message = Messages.PATIENT_PHONE_INVALID)
        String phone) {
}
