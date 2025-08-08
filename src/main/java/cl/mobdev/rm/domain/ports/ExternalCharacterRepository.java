package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;
import java.util.List;

public interface ExternalCharacterRepository {
  Character findCharacter(String id);

  List<Character> getAllCharacters();
}
