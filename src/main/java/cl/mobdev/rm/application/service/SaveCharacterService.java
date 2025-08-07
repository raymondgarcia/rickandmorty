package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.LocalCharacterRepository;
import cl.mobdev.rm.domain.ports.SaveCharacterUseCase;
import org.springframework.stereotype.Service;

@Service
public class SaveCharacterService implements SaveCharacterUseCase {

  private final LocalCharacterRepository repository;

  public SaveCharacterService(LocalCharacterRepository repository) {
    this.repository = repository;
  }

  @Override
  public Character saveCharacter(Character character) {
    return repository.save(character);
  }
}
