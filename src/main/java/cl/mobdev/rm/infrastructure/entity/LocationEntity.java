package cl.mobdev.rm.infrastructure.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;
  private String url;
  private String dimension;

  @ElementCollection
  @CollectionTable(name = "location_residents", joinColumns = @JoinColumn(name = "location_id"))
  @Column(name = "resident_url")
  private List<String> residents;
}
