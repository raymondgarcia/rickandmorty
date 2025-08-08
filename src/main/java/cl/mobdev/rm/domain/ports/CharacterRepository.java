package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;

public interface CharacterRepository {
  Character save(Character character);
}
