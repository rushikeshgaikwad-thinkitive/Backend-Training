package bt.com.dto.module;

import bt.com.dto.constants.Messages;
import jakarta.validation.constraints.NotBlank;
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
public class AppointmentCancellation {

    @NotBlank(
            message = Messages.CANCELLATION_REASON_REQUIRED
    )
    @Size(
            max = 500,
            message = Messages.CANCELLATION_REASON_MAX_LENGTH
    )
    private String cancellationReason;
}