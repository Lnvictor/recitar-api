package ccb.smonica.recitar_api.dto;

public record OIDCLoginResponseDTO(
    String username,
    String accessToken,
    Integer expiresIn
) {
    
}
