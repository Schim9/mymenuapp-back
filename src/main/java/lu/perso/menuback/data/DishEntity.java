package lu.perso.menuback.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "dishes")
public record DishEntity(@Id Long id, String name, List<IngredientEntity> recipe) { }
