package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.IsCharactersEarthlingUseCase;
import org.springframework.stereotype.Service;

@Service
public class IsCharactersEarthlingService implements IsCharactersEarthlingUseCase {
  @Override
  public boolean execute(Character character) {
    return character
        .location()
        .filter(location -> location.name() != null && !location.name().isEmpty())
        .map(location -> location.dimension().toLowerCase().contains("earth"))
        .orElse(false);
  }
}
