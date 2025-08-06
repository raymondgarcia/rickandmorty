package cl.mobdev.rm.application.service;

import cl.mobdev.rm.domain.ports.FindCharacterById;
import cl.mobdev.rm.domain.ports.RickAndMortyApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.mobdev.rm.domain.model.Character;

import java.util.Optional;

@Service
public class FindCharacterByIdImpl implements FindCharacterById {

    @Autowired
    RickAndMortyApiClient client;

    @Override
    public Optional<Character> execute(String id) {
         return Optional.of(client.getChararcter(id));
    }
}
