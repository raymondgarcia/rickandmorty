package cl.mobdev.rm.application.service;

import cl.mobdev.rm.application.dto.CharacterResponse;
import cl.mobdev.rm.application.dto.OriginResponse;
import cl.mobdev.rm.application.ports.FindCharacterById;
import cl.mobdev.rm.application.ports.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.mobdev.rm.domain.model.Character;

import java.util.Optional;

@Service
public class FindCharacterByIdImpl implements FindCharacterById {

    @Autowired
    ApiClient client;

    @Override
    public CharacterResponse execute(String id) {
         return mapperToCharacterResponse(client.getChararcter(id));
    }

    private static CharacterResponse mapperToCharacterResponse(Character character) {
        var origin = getOriginResponse(character);
        return new CharacterResponse(character.id(), character.name(),
                character.status(), character.species(),
                character.type(), character.episodeCount(), origin);
    }

    private static Optional<OriginResponse> getOriginResponse(Character character) {
        return character.location()
                .map( origin -> new OriginResponse(origin.name(), origin.url(), origin.dimension(), origin.residents()));
    }
}
