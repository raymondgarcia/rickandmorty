package cl.mobdev.rm.infrastructure.adapter.inbound;

import cl.mobdev.rm.application.ports.FindCharacterById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.mobdev.rm.application.dto.CharacterResponse;

@RestController
@RequestMapping("/api/v1/characters")
public class CharacterController {

    @Autowired
    FindCharacterById findCharacterById;

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> getCharacterInformation(@PathVariable String id) {
        return ResponseEntity.ok(findCharacterById.execute(id));
    }

}
