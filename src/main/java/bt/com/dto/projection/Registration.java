package bt.com.dto.projection;


import bt.com.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Registration {

    private Long userId;

    private String email;

    private Role role;

    private String status;

    private String message;
}
