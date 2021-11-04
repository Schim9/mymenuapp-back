package lu.perso.menuback.services;

import lu.perso.menuback.constant.MenuEnum;
import lu.perso.menuback.data.IngredientEntity;
import lu.perso.menuback.mappers.IngredientMapper;
import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.repository.DishRepository;
import lu.perso.menuback.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IngredientServices {
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DishRepository dishRepository;
    @Autowired
    DatabaseServices databaseServices;
    @Autowired
    IngredientMapper ingredientMapper;

    public List<Ingredient> getAllIngredients() {
        List<IngredientEntity> ingredientList = ingredientRepository.findAll();
        return ingredientList.stream()
                .map(element -> ingredientMapper.toView(element))
                .collect(Collectors.toList());
    }

    public Ingredient createIngredient(Ingredient ingredientModel) throws IllegalStateException{

        // Check if ingredient already exists
        if (!Objects.isNull(ingredientRepository.findByName(ingredientModel.name()))) {
            throw new IllegalStateException("The ingredient does already  exist");
        }
        IngredientEntity newIngredient = new IngredientEntity(
                databaseServices.generateSequence(MenuEnum.SEQUENCE_TYPE.INGREDIENTS),
                StringUtils.capitalize(ingredientModel.name()),
                ingredientModel.sectionId(),
                ingredientModel.unit()
        );
        IngredientEntity savedObject = ingredientRepository.save(newIngredient);
        return ingredientMapper.toView(savedObject);
    }

    public Ingredient updateIngredient(Ingredient updatedIngredient) throws IllegalStateException {
        // Check if ingredient does exist
        if (ingredientRepository.findById(updatedIngredient.id()).isEmpty()) {
            throw new IllegalStateException("Thie ingredient does not exist");
        }

        IngredientEntity newIngredient = new IngredientEntity(
                updatedIngredient.id(),
                StringUtils.capitalize(updatedIngredient.name()),
                updatedIngredient.sectionId(),
                updatedIngredient.unit()
        );
        ingredientRepository.save(newIngredient);
        return updatedIngredient;
    }

    public void deleteIngredient(Ingredient ingredientToDelete) throws IllegalStateException {
        // Check if ingredient does exist
        IngredientEntity storedIngredient = ingredientRepository.findById(ingredientToDelete.id())
                .orElseThrow(() -> new IllegalStateException("This ingredient does not exist"));
        // Check if ingredient is not used in any dish
        if (dishRepository.findDishEntitiesByRecipeContaining(Stream.of(storedIngredient).toList()).isEmpty()) {
            ingredientRepository.deleteById(ingredientToDelete.id());
        } else {
            throw new IllegalStateException("This ingredient is used and can not be deleted");
        }
    }
}
