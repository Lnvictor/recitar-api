package ccb.smonica.recitar_api.handler;

import ccb.smonica.recitar_api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserNotFoundExceptionDetails> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(UserNotFoundExceptionDetails.builder()
                .title("User Not Found")
                .detail("The User who requested new roles was not found in the database")
                .username(null)
                .timestamp(LocalDateTime.now())
                .status(404)
                .build(), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<RoleNotFoundExceptionDetails> handleRoleNotFoundException(RoleNotFoundException ex) {
        return new ResponseEntity<>(
                RoleNotFoundExceptionDetails.builder()
                        .title("Role Not Found")
                        .detail("Role requested for user was not found")
                        .roleName(null)
                        .timestamp(LocalDateTime.now())
                        .status(404)
                        .build(), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(
                Map.of("message", ex.getMessage())
        );
    }
}
