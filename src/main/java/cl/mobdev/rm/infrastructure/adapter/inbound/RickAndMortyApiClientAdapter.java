package cl.mobdev.rm.infrastructure.adapter.inbound;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.model.Location;
import cl.mobdev.rm.domain.ports.RickAndMortyApiClient;
import cl.mobdev.rm.infrastructure.dto.CharacterApiDto;
import cl.mobdev.rm.infrastructure.dto.LocationApiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RickAndMortyApiClientAdapter implements RickAndMortyApiClient {

    @Autowired
    RestClient restClient;

    @Override
    public Character getChararcter(String id) {
        CharacterApiDto chardto = restClient.get().uri("character/{id}", id).retrieve().body(CharacterApiDto.class);
        String url = chardto.origin().url();
        String locationId = url.substring(url.lastIndexOf("/")+1);
        LocationApiDto locDto  = restClient.get().uri("location/{id}", locationId).retrieve().body(LocationApiDto.class);
        return toDomain(chardto, locDto);
    }

    private Character toDomain(CharacterApiDto chardto, LocationApiDto locDto) {
        Location location = new Location(locDto.name(), chardto.origin().url(), locDto.dimension(), locDto.residents());
        Integer episodeCount = chardto.episode().size();
        return new Character(chardto.id(),
                chardto.name(),
                chardto.status(),
                chardto.species(), chardto.type(), episodeCount, location);
    }


}
