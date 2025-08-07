package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;

public interface LocalCharacterRepository {
  Character save(Character character);
}
