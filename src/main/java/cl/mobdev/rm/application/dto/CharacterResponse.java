package cl.mobdev.rm.application.dto;


import java.util.Optional;

public record CharacterResponse(Integer id,
                                String name,
                                String status,
                                String species,
                                String type,
                                Integer episode_count,
                                Optional<OriginResponse> origin) {
}
