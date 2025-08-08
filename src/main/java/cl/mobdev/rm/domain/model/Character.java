package cl.mobdev.rm.domain.model;

import java.util.Optional;

public record Character(
    Integer id,
    String name,
    String status,
    String species,
    String type,
    Integer episodeCount,
    Optional<Location> location) {

  public Character withType(String type) {
    return new Character(id, name, status, species, type, episodeCount, location);
  }
}
