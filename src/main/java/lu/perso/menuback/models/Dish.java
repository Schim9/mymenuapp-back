package lu.perso.menuback.models;

import java.util.List;

public record Dish(Long id, String name, List<Long> recipe) implements MenuItem { }
