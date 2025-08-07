package cl.mobdev.rm.infrastructure.adapter.outbound;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.CharacterRepository;
import cl.mobdev.rm.infrastructure.client.RickAndMortyHttpClient;
import cl.mobdev.rm.infrastructure.dto.CharacterApiDto;
import cl.mobdev.rm.infrastructure.dto.LocationApiDto;
import cl.mobdev.rm.infrastructure.mapper.CharacterDomainMapper;
import org.springframework.stereotype.Component;

@Component
public class RickAndMortyCharacterRepositoryAdapter implements CharacterRepository {

  private final RickAndMortyHttpClient httpClient;

  public RickAndMortyCharacterRepositoryAdapter(RickAndMortyHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public Character getChararcter(String id) {
    CharacterApiDto characterDto = httpClient.getCharacterApiDto(id);
    return characterDto
        .origin()
        .map(
            origin -> {
              String url = origin.url();
              String locationId = exctractLocationID(url);

              LocationApiDto locDto = httpClient.getLocationApiDto(locationId);

              return CharacterDomainMapper.toDomain(characterDto, locDto);
            })
        .orElse(CharacterDomainMapper.toDomain(characterDto));
  }

  private static String exctractLocationID(String url) {
    int locationIdIndex = 1;

    return url.substring(url.lastIndexOf("/") + locationIdIndex);
  }
}
