package ccb.smonica.recitar_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "youth_cult")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouthCult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cultId;

    @Column(name = "day", nullable = false)
    private String day;

    @Column(name = "month", nullable = false)
    private String month;

    @Column(name = "year", nullable = false)
    private String year;
}
