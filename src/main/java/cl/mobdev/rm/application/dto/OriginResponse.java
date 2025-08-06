package cl.mobdev.rm.application.dto;


import java.util.List;

public record OriginResponse(String name,
                             String url,
                             String dimension,
                             List<String> residents) {

}
