package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;
import java.util.Optional;

public interface CharacterRepository {
  Character save(Character character);

  Optional<Character> findByApiCharacterId(Integer id);

  boolean existByApiCharacterId(Integer id);
}
