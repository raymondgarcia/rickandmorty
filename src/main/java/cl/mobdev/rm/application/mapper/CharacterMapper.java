package cl.mobdev.rm.application.mapper;

import cl.mobdev.rm.application.dto.CharacterRequest;
import cl.mobdev.rm.application.dto.CharacterResponse;
import cl.mobdev.rm.application.dto.OriginResponse;
import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import java.util.Optional;

public class CharacterMapper {

  private CharacterMapper() {}

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

  public static Character mapperToCharacter(CharacterRequest characterRequest) {
    Optional<Location> origin = getLocation(characterRequest);
    return new Character(
        characterRequest.id(),
        characterRequest.name(),
        characterRequest.status(),
        characterRequest.species(),
        characterRequest.type(),
        characterRequest.episode_count(),
        origin);
  }

  private static Optional<Location> getLocation(CharacterRequest characterRequest) {
    return characterRequest
        .origin()
        .map(
            o ->
                new cl.mobdev.rm.domain.model.Location(
                    o.name(), o.url(), o.dimension(), o.residents()));
  }
}
