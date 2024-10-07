package ccb.smonica.recitar_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OIDCProviderTokenResponseDTO(
    @JsonProperty(value = "access_token")
    String accessToken

    // @JsonProperty(value = "id_token")
    // String idToken
) {
}
