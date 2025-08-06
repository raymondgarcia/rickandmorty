package cl.mobdev.rm.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CharacterApiDto(Integer id,
                              String name,
                              String status,
                              String species,
                              String type,
                              String gender,
                              SimpleResource origin,
                              SimpleResource location,
                              List<String> episode) {

    public record SimpleResource(String name, String url) {}

}
