package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.ports.FindCharacterUseCase;
import cl.mobdev.rm.domain.ports.CharacterRepository;
import cl.mobdev.rm.domain.model.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindCharacterService implements FindCharacterUseCase {

    @Autowired
    CharacterRepository client;

    @Override
    public Character execute(String id) {
        return client.getChararcter(id);
    }
}
