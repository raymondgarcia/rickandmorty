package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;

@FunctionalInterface
public interface CharacterRepository {
    Character getChararcter(String id);
}
