
package bt.com.dto.module;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegistrationRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 12, max = 72)
    private String password;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String phone;

    @NotBlank
    @Size(max = 100)
    private String specialization;
}
