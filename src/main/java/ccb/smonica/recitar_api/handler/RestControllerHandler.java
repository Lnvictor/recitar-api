package ccb.smonica.recitar_api.handler;

import ccb.smonica.recitar_api.exception.CultsNotFoundException;
import ccb.smonica.recitar_api.exception.CultsNotFoundExceptionDetails;
import ccb.smonica.recitar_api.exception.ReportFileNotFoundException;
import ccb.smonica.recitar_api.exception.ReportNotFoundDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestControllerHandler {
    @ExceptionHandler(CultsNotFoundException.class)
    public ResponseEntity<CultsNotFoundExceptionDetails> handleCultsNotFoundException(CultsNotFoundException ex) {
        return new ResponseEntity<>(CultsNotFoundExceptionDetails.builder()
                .details("Any Cult was found to return")
                .title("Cults not")
                .timestamp(LocalDateTime.now())
                .status(400)
                .developerMessage("Nothing to declare")
                .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ReportFileNotFoundException.class)
    public ResponseEntity<ReportNotFoundDetails> handleReportFileNotFoundException(ReportFileNotFoundException ex) {
        return new ResponseEntity<>(ReportNotFoundDetails.builder()
                .message("Report Not Found to generate PDF")
                .fileName(ex.getFileName())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
