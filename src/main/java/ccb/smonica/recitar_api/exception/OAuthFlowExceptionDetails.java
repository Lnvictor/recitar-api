package ccb.smonica.recitar_api.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OAuthFlowExceptionDetails {
    private String title;
    private int status;
    private String details;
    private String developerMessage;
    private String authProvider;
    private LocalDateTime timestamp;
}
