package cl.mobdev.rm.application.dto;

public record CharacterResponse(Integer id,
                                String name,
                                String status,
                                String species,
                                String type,
                                Integer episode_count,
                                OriginResponse origin) {
}
