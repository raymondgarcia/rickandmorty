package cl.mobdev.rm.application.ports;

import cl.mobdev.rm.domain.model.Character;

public interface ApiClient {
    Character getChararcter(String id);
}
