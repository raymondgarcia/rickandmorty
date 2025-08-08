package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;

@FunctionalInterface
public interface IsCharactersEarthlingUseCase {
  boolean execute(Character character);
}
