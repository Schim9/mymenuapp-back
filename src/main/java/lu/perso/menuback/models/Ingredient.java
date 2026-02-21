package lu.perso.menuback.models;

import lu.perso.menuback.constant.MenuEnum.UNIT;

public record Ingredient(Long id, String name, Long sectionId, UNIT unit, Boolean isDish) implements MenuItem { }
