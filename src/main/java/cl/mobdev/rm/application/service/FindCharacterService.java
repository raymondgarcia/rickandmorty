package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.ExternalCharacterRepository;
import cl.mobdev.rm.domain.ports.FindCharacterUseCase;
import org.springframework.stereotype.Service;

@Service
public class FindCharacterService implements FindCharacterUseCase {

  private final ExternalCharacterRepository client;

  public FindCharacterService(ExternalCharacterRepository client) {
    this.client = client;
  }

  @Override
  public Character execute(String id) {
    return client.findCharacter(id);
  }
}
