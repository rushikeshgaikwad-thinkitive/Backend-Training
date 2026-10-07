package bt.com.dto.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class ErrorResponse {

	private Instant timestamp;
	private int  status;
	private String error;
	private String message;
	private String path;
}   

