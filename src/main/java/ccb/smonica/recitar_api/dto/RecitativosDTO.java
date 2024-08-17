package ccb.smonica.recitar_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecitativosDTO {
    private Integer girls;
    private Integer boys;
    private Integer youthGirls;
    private Integer youthBoys;
    private Integer individuals;
    private Integer total;
}
