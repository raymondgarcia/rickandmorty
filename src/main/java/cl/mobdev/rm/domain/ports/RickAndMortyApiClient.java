package cl.mobdev.rm.domain.ports;

import cl.mobdev.rm.domain.model.Character;

public interface RickAndMortyApiClient {
    Character getChararcter(String id);
}
