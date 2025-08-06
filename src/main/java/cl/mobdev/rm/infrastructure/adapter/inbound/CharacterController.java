package cl.mobdev.rm.infrastructure.adapter.inbound;


import cl.mobdev.rm.application.dto.OriginResponse;
import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.FindCharacterById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.mobdev.rm.application.dto.CharacterResponse;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/characters")
public class CharacterController {

    @Autowired
    FindCharacterById findCharacterById;

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getCharacterInformation(@PathVariable String id) {
        return findCharacterById.execute(id)
                .map(CharacterController::mapperToCharacterResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
