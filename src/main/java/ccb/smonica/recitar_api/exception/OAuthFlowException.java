package ccb.smonica.recitar_api.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthFlowException extends RuntimeException{
    public OAuthFlowException(){

    }

    public OAuthFlowException(String message) {
        super(message);
    }
}
