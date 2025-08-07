package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;

@FunctionalInterface
public interface FindCharacterUseCase {
    Character execute(String id);
}
