package bt.com.dto.response;

import java.time.Instant;
import java.time.LocalDate;

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
public class PatientResponse {

    private Long id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String email;
    private boolean active;

    private Instant createdAt;
    private Instant updatedAt;

    private LocalDate dateOfBirth;
    private String formattedDob;
}
