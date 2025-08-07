package cl.mobdev.rm.domain.model;

import java.util.Optional;

public record Character(
    Integer id,
    String name,
    String status,
    String species,
    String type,
    Integer episodeCount,
    Optional<Location> location) {}
