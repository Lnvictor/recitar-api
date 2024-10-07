package ccb.smonica.recitar_api.exception;


import lombok.Getter;
import lombok.Setter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
@Setter
public class ReportFileNotFoundException extends RuntimeException {
    private String fileName;

    public ReportFileNotFoundException() {
    }

    public ReportFileNotFoundException(String message) {
        super(message);
    }

    public ReportFileNotFoundException(String message, String fileName) {
        super(message);
        this.fileName = fileName;
    }
}
