package bt.com.dto.module;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRegistrationRequest {

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
    @Size(max = 20)
    private String gender;

    @NotNull
    @Past
    private LocalDate dateOfBirth;
}
