package ccb.smonica.recitar_api.dto;

import ccb.smonica.recitar_api.enums.RoleName;

public record UserAccessDTO(
        RoleName role,
        String username
) {

}
