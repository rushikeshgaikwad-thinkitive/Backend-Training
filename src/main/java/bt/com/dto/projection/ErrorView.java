package bt.com.dto.projection;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ErrorView {

    private Instant timestamp;

    private int status;

    private String error;

    private String message;

    private String path;
}