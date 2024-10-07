package ccb.smonica.recitar_api.dto;

public record PostLoginResponseDTO (
        String token,
        Integer expiresIn
)
{}
