package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.ports.ExternalCharacterRepository;
import cl.mobdev.rm.domain.ports.IsHumanCharacterUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IsHumanCharacterService implements IsHumanCharacterUseCase {

  @Autowired ExternalCharacterRepository client;

  public IsHumanCharacterService(ExternalCharacterRepository client) {
    this.client = client;
  }

  @Override
  public boolean execute(String characterId) {
    return client.findCharacter(characterId).species().equalsIgnoreCase("Human");
  }
}
