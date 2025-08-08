package cl.mobdev.rm.infrastructure.mapper;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import cl.mobdev.rm.infrastructure.entity.CharacterEntity;
import cl.mobdev.rm.infrastructure.entity.LocationEntity;
import java.util.List;
import java.util.Optional;

public final class CharacterDomainMapper {

  private CharacterDomainMapper() {}

  public static Character toDomain(CharacterEntity entity) {
    if (entity == null) {
      return null;
    }

    Optional<Location> location =
        Optional.ofNullable(entity.getLocation())
            .map(CharacterDomainMapper::locationEntityToDomain);

    return new Character(
        entity.getApiCharacterId(),
        entity.getName(),
        entity.getStatus(),
        entity.getSpecies(),
        entity.getType(),
        entity.getEpisodeCount(),
        location);
  }

  public static CharacterEntity toEntity(Character character) {
    if (character == null) {
      return null;
    }

    CharacterEntity entity = new CharacterEntity();
    entity.setApiCharacterId(character.id());
    entity.setName(character.name());
    entity.setStatus(character.status());
    entity.setSpecies(character.species());
    entity.setType(character.type());
    entity.setEpisodeCount(character.episodeCount());

    // Handle location relationship
    character
        .location()
        .ifPresent(
            location -> {
              LocationEntity locationEntity = locationDomainToEntity(location);
              entity.setLocation(locationEntity);
            });

    return entity;
  }

  private static Location locationEntityToDomain(LocationEntity locationEntity) {
    if (locationEntity == null) {
      return null;
    }

    return new Location(
        locationEntity.getName(),
        locationEntity.getUrl(),
        locationEntity.getDimension(),
        locationEntity.getResidents() != null ? locationEntity.getResidents() : List.of());
  }

  private static LocationEntity locationDomainToEntity(Location location) {
    if (location == null) {
      return null;
    }

    LocationEntity entity = new LocationEntity();
    entity.setName(location.name());
    entity.setUrl(location.url());
    entity.setDimension(location.dimension());
    entity.setResidents(location.residents());

    return entity;
  }
}
