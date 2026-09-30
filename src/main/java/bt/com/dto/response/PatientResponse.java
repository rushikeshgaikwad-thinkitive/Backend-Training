package bt.com.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatientResponse {

    private Long id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String email;
    private boolean active;
}