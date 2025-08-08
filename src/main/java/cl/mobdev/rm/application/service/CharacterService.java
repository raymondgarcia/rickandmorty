package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.CharacterRepository;
import cl.mobdev.rm.domain.ports.ExternalCharacterRepository;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

  private final ExternalCharacterRepository externalRepo;
  private final CharacterRepository localRepo;

  public CharacterService(ExternalCharacterRepository externalRepo, CharacterRepository localRepo) {
    this.externalRepo = externalRepo;
    this.localRepo = localRepo;
  }

  public Character findCharacter(String id) {
    return externalRepo.findCharacter(id);
  }

  public Character saveToLocal(Character character) {
    return localRepo.save(character);
  }
}
