package cl.mobdev.rm.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "characters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterEntity {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  Integer id;

  String name;

  String status;

  String species;

  String type;

  Integer episodeCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id")
  LocationEntity location;
}
