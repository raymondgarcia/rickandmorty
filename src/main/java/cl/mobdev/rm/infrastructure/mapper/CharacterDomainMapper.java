package cl.mobdev.rm.infrastructure.mapper;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import cl.mobdev.rm.infrastructure.dto.CharacterApiDto;
import cl.mobdev.rm.infrastructure.dto.LocationApiDto;
import java.util.List;
import java.util.Optional;

public final class CharacterDomainMapper {

  private CharacterDomainMapper() {}

  public static Character toDomain(CharacterApiDto dto) {
    Integer episodeCount = dto.episode() != null ? dto.episode().size() : 0;

    return new Character(
        dto.id(),
        dto.name(),
        dto.status(),
        dto.species(),
        dto.type(),
        episodeCount,
        Optional.empty());
  }

  public static Character toDomain(CharacterApiDto characterDto, LocationApiDto locationDto) {
    Integer episodeCount = characterDto.episode() != null ? characterDto.episode().size() : 0;

    Optional<Location> location =
        characterDto
            .origin()
            .map(
                origin ->
                    new Location(
                        locationDto.name(),
                        origin.url(),
                        locationDto.dimension(),
                        locationDto.residents() != null ? locationDto.residents() : List.of()));

    return new Character(
        characterDto.id(),
        characterDto.name(),
        characterDto.status(),
        characterDto.species(),
        characterDto.type(),
        episodeCount,
        location);
  }
}
