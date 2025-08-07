package cl.mobdev.rm.infrastructure.adapter.inbound;

import cl.mobdev.rm.application.mapper.CharacterMapper;
import cl.mobdev.rm.domain.ports.FindCharacterUseCase;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.mobdev.rm.application.dto.CharacterResponse;
import cl.mobdev.rm.domain.model.Character;

@RestController
@RequestMapping("/api/v1/character")
public class CharacterController {

    @Autowired
    FindCharacterUseCase findCharacterById;

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getCharacterInformation(@PathVariable @Positive String id) {
        Character character = findCharacterById.execute(id);
        CharacterResponse response = CharacterMapper.mapperToCharacterResponse(character);
        return ResponseEntity.ok(response);
    }

}
