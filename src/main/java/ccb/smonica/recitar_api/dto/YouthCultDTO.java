package ccb.smonica.recitar_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class YouthCultDTO {
    private LocalDate date;
    private RecitativosDTO recitativos;
}
