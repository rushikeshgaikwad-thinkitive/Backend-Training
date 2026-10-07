package bt.com.dto.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequest {

    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String email;
    private boolean active;
}