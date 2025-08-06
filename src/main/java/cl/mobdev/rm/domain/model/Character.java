package cl.mobdev.rm.domain.model;

public record Character (Integer id,
                         String name,
                         String status,
                         String species,
                         String type,
                         Integer episodeCount,
                         Location location
){

}
