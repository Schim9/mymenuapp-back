package lu.perso.menuback.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lu.perso.menuback.mappers.MenuItemDeserializer;

@JsonDeserialize(using = MenuItemDeserializer.class)
public sealed interface MenuItem permits Dish, Ingredient {
}
