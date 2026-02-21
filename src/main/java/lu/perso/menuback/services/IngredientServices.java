package lu.perso.menuback.services;

import lu.perso.menuback.data.IngredientEntity;
import lu.perso.menuback.data.MenuItemEntity;
import lu.perso.menuback.mappers.IngredientMapper;
import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.repository.DishRepository;
import lu.perso.menuback.repository.IngredientRepository;
import lu.perso.menuback.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class IngredientServices {

    private final IngredientRepository ingredientRepository;
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientServices(IngredientRepository ingredientRepository, DishRepository dishRepository,
                              MenuRepository menuRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.dishRepository = dishRepository;
        this.menuRepository = menuRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public List<Ingredient> getAllIngredients() {
        List<IngredientEntity> ingredientList = ingredientRepository.findAll();
        return ingredientList.stream()
                .map(element -> ingredientMapper.toView(element))
                .collect(Collectors.toList());
    }

    public Ingredient createIngredient(Ingredient ingredientModel) throws IllegalStateException {

        // Check if ingredient already exists
        if (!Objects.isNull(ingredientRepository.findByName(ingredientModel.name()))) {
            throw new IllegalStateException("The ingredient does already  exist");
        }
        IngredientEntity newIngredient = new IngredientEntity(
                null,
                StringUtils.capitalize(ingredientModel.name()),
                ingredientModel.sectionId(),
                ingredientModel.unit(),
                ingredientModel.isDish()
        );
        IngredientEntity savedObject = ingredientRepository.save(newIngredient);
        return ingredientMapper.toView(savedObject);
    }

    public void updateIngredient(Long ingredientId, Ingredient updatedIngredient) throws IllegalStateException {
        // Check if ingredient does exist
        if (ingredientRepository.findById(ingredientId).isEmpty()) {
            throw new IllegalStateException("This ingredient does not exist");
        }

        IngredientEntity newIngredient = new IngredientEntity(
                ingredientId,
                StringUtils.capitalize(updatedIngredient.name()),
                updatedIngredient.sectionId(),
                updatedIngredient.unit(),
                updatedIngredient.isDish()
        );
        ingredientRepository.save(newIngredient);
    }

    public void deleteIngredient(Long ingredientId) throws IllegalStateException {
        // Check if ingredient does exist
        IngredientEntity storedIngredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalStateException("This ingredient does not exist"));
        // Check if ingredient is not used in any dish
        List<MenuItemEntity> elements = Stream.of((MenuItemEntity) storedIngredient).toList();
        if (!dishRepository.findDishEntitiesByIngredientsContaining(Stream.of(storedIngredient).toList()).isEmpty()) {
            throw new IllegalStateException("This ingredient is used and can not be deleted");
        } else if (!menuRepository.findMenus(elements, elements).isEmpty()) {
            throw new IllegalStateException("This ingredient is planned and can not be deleted");
        } else {
            ingredientRepository.deleteById(ingredientId);
        }
    }
}
