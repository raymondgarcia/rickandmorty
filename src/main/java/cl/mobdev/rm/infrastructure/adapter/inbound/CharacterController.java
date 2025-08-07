package cl.mobdev.rm.infrastructure.adapter.inbound;

import cl.mobdev.rm.application.dto.CharacterRequest;
import cl.mobdev.rm.application.dto.CharacterResponse;
import cl.mobdev.rm.application.mapper.CharacterMapper;
import cl.mobdev.rm.application.service.CharacterService;
import cl.mobdev.rm.domain.model.Character;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/character")
public class CharacterController {

  @Autowired CharacterService service;

  @GetMapping("/{id}")
  public ResponseEntity<CharacterResponse> getCharacterInformation(
      @PathVariable @Positive String id) {
    Character character = service.findCharacter(id);
    CharacterResponse response = CharacterMapper.mapperToCharacterResponse(character);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CharacterResponse> saveCharacter(@RequestBody CharacterRequest request) {
    Character character = CharacterMapper.mapperToCharacter(request);
    Character savedCharacter = service.saveToLocal(character);
    CharacterResponse response = CharacterMapper.mapperToCharacterResponse(savedCharacter);
    return ResponseEntity.ok(response);
  }
}
