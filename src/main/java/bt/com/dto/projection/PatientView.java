package bt.com.dto.projection;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PatientView {

    private Long id;

    private String name;

    private Integer age;

    private String gender;

    private String phone;

    private String email;

    private boolean active;

    private Instant createdAt;

    private Instant updatedAt;
}