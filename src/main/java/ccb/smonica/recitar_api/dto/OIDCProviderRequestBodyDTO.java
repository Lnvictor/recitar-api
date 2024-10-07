package ccb.smonica.recitar_api.dto;

public record OIDCProviderRequestBodyDTO(
    String code,
    String grant_type,
    String redirect_uri
) {
}