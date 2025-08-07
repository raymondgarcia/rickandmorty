package cl.mobdev.rm.infrastructure.dto;

import java.util.List;

public record LocationApiDto(
    long id, String name, String type, String dimension, List<String> residents) {}
