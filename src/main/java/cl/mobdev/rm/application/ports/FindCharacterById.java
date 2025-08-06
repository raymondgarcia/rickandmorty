package cl.mobdev.rm.application.ports;

import cl.mobdev.rm.application.dto.CharacterResponse;

@FunctionalInterface
public interface FindCharacterById {
    CharacterResponse execute(String id);
}
