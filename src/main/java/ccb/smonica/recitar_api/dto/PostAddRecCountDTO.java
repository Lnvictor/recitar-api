package ccb.smonica.recitar_api.dto;

import java.time.LocalDate;

public record PostAddRecCountDTO(
        LocalDate cultDate,
        Integer girls,
        Integer boys,
        Integer youthGirls,
        Integer youthBoys,
        Integer individuals
) {

}
