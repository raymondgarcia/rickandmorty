package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.model.Character;
import cl.mobdev.rm.domain.ports.ExternalCharacterRepository;
import cl.mobdev.rm.domain.ports.FindCharacterUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindCharacterService implements FindCharacterUseCase {

  @Autowired ExternalCharacterRepository client;

  @Override
  public Character execute(String id) {
    return client.findCharacter(id);
  }
}
