package bt.com.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRequest {

    private String name;

    private String specialization;

    private String phone;

    private String email;

    private boolean active;
}