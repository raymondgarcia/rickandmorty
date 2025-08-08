package cl.mobdev.rm.infrastructure.adapter.outbound;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.ExternalCharacterRepository;
import cl.mobdev.rm.infrastructure.client.RickAndMortyHttpClient;
import cl.mobdev.rm.infrastructure.dto.CharacterApiDto;
import cl.mobdev.rm.infrastructure.dto.LocationApiDto;
import cl.mobdev.rm.infrastructure.mapper.CharacterEntityMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RickAndMortyExternalCharacterRepositoryAdapter implements ExternalCharacterRepository {

  private final RickAndMortyHttpClient httpClient;

  public RickAndMortyExternalCharacterRepositoryAdapter(RickAndMortyHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public Character findCharacter(String id) {
    CharacterApiDto characterDto = httpClient.getCharacterApiDto(id);
    return characterDto
        .origin()
        .map(
            origin -> {
              String url = origin.url();
              String locationId = exctractLocationID(url);

              LocationApiDto locDto = httpClient.getLocationApiDto(locationId);

              return CharacterEntityMapper.toDomain(characterDto, locDto);
            })
        .orElse(CharacterEntityMapper.toDomain(characterDto));
  }

  @Override
  public List<Character> getAllCharacters() {
    return List.of();
  }

  private static String exctractLocationID(String url) {
    int locationIdIndex = 1;

    return url.substring(url.lastIndexOf("/") + locationIdIndex);
  }
}
