package cl.mobdev.rm.domain.ports;

@FunctionalInterface
public interface IsHumanCharacterUseCase {
  boolean execute(String characterId);
}
