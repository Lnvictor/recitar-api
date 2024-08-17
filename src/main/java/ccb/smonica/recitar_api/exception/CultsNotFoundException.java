package ccb.smonica.recitar_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CultsNotFoundException extends RuntimeException {
    public CultsNotFoundException(String message) {
        super(message);
    }
}
