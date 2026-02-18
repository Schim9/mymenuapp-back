package lu.perso.menuback.models;

import lu.perso.menuback.constant.MenuEnum.UNIT;

import java.util.Optional;

public record Ingredient(Long id, String name, Long sectionId, UNIT unit) implements MenuItem { }
