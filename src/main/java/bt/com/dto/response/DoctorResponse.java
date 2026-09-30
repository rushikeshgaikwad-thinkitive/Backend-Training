package bt.com.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorResponse {

    private Long id;

    private String name;

    private String specialization;

    private String phone;

    private String email;

    private boolean active;
}