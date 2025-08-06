package cl.mobdev.rm.domain.model;

import java.util.List;

public record Location(String name, String url, String dimension, List<String> residents) {
}
