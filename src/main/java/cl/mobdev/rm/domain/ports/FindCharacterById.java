package cl.mobdev.rm.domain.ports;

import java.util.Optional;
import cl.mobdev.rm.domain.model.Character;

@FunctionalInterface
public interface FindCharacterById {
    Optional<Character> execute(String id);
}
