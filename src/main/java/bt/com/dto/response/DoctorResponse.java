package bt.com.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long id;

    private String name;

    private String specialization;

    private String phone;

    private String email;

    private boolean active;
    
    private Instant createdAt;
    
    private Instant updatedAt;
}