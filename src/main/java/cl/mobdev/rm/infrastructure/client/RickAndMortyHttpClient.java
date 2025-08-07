package cl.mobdev.rm.infrastructure.client;

import cl.mobdev.rm.infrastructure.dto.CharacterApiDto;
import cl.mobdev.rm.infrastructure.dto.EpisodeApiDto;
import cl.mobdev.rm.infrastructure.dto.LocationApiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RickAndMortyHttpClient {
  @Autowired RestClient restClient;

  public CharacterApiDto getCharacterApiDto(String id) {
    return restClient.get().uri("character/{id}", id).retrieve().body(CharacterApiDto.class);
  }

  public LocationApiDto getLocationApiDto(String id) {
    return restClient.get().uri("location/{id}", id).retrieve().body(LocationApiDto.class);
  }

  public EpisodeApiDto getEpisodeApiDto(String id) {
    return restClient.get().uri("episode/{id}", id).retrieve().body(EpisodeApiDto.class);
  }
}
