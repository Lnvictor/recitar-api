package ccb.smonica.recitar_api.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportNotFoundDetails {
    private String message;
    private String fileName;
}
