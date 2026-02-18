package lu.perso.menuback.data;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class DishEntity extends MenuItemEntity {
        @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinTable(
                name = "dish_ingredient",
                joinColumns = @JoinColumn(name = "dish_id"),
                inverseJoinColumns = @JoinColumn(name = "ingredient_id")
        )
        List<IngredientEntity> ingredients;

        // Getter et setter
        public List<IngredientEntity> getIngredients() { return ingredients; }
        public void setIngredients(List<IngredientEntity> ingredients) { this.ingredients = ingredients; }

        public DishEntity() {
                super();
        }

        public DishEntity(Long id, String name, List<IngredientEntity> ingredients) {
                super();
                this.setId(id);
                this.setName(name);
                this.setIngredients(ingredients);
        }
}