package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;

public interface ExternalCharacterRepository {
  Character findCharacter(String id);
}
