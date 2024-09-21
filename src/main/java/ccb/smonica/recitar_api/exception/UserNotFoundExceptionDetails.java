package ccb.smonica.recitar_api.exception;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserNotFoundExceptionDetails {
    private String title;
    private String detail;
    private String username;
    private int status;
    private LocalDateTime timestamp;
}
