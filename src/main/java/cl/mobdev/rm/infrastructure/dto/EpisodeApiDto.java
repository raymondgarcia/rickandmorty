package cl.mobdev.rm.infrastructure.dto;

import java.util.List;

public record EpisodeApiDto(
    String id,
    String name,
    String airDate,
    String episode,
    String url,
    String created,
    List<String> characters) {}
