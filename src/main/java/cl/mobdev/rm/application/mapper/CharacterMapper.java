package cl.mobdev.rm.application.mapper;

import cl.mobdev.rm.application.dto.CharacterResponse;
import cl.mobdev.rm.application.dto.OriginResponse;
import cl.mobdev.rm.domain.model.Character;
import java.util.Optional;

public class CharacterMapper {
  public static CharacterResponse mapperToCharacterResponse(
      cl.mobdev.rm.domain.model.Character character) {
    var origin = getOriginResponse(character);
    return new CharacterResponse(
        character.id(),
        character.name(),
        character.status(),
        character.species(),
        character.type(),
        character.episodeCount(),
        origin);
  }

  public static Optional<OriginResponse> getOriginResponse(Character character) {
    return character
        .location()
        .map(
            origin ->
                new OriginResponse(
                    origin.name(), origin.url(), origin.dimension(), origin.residents()));
  }
}
