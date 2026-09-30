package bt.com.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequest {

    private String name;
    private int age;
    private String gender;
    private String phone;
    private String email;
    private boolean active;
}