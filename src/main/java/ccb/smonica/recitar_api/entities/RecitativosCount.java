package ccb.smonica.recitar_api.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recitativos_count")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RecitativosCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cult_id")
    private YouthCult youthCult;

    @Column(name = "girls")
    private int girls;

    @Column(name = "boys")
    private  int boys;

    @Column(name = "young_girls")
    private int youngGirls;

    @Column(name = "young_boys")
    private int youngBoys;

    @Column(name = "individuals")
    private int individuals;
}
