package lu.perso.menuback.services;

import lu.perso.menuback.constant.MenuEnum;
import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.mappers.DishMapper;
import lu.perso.menuback.models.Dish;
import lu.perso.menuback.repository.DishRepository;
import lu.perso.menuback.repository.IngredientRepository;
import lu.perso.menuback.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DishServices {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    DishRepository dishRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DatabaseServices databaseServices;

    public List<Dish> getAllDishes() {
        List<DishEntity> dishList = dishRepository.findAll();
        return dishList.stream()
                .map(element -> dishMapper.toView(element))
                .collect(Collectors.toList());
    }

    public Dish createDish(Dish newDish) throws IllegalStateException {
        // Check if dish already exists
        if (!Objects.isNull(dishRepository.findByName(newDish.name()))) {
           throw new IllegalStateException(newDish.name() + " does already exist");
        }
        // Get Ingredients from database
        // In case one of them does not exist, an error is raised
        var recipe = newDish.recipe()
                .stream()
                .map(
                    ingredient -> ingredientRepository.findById(ingredient.id())
                        .orElseThrow(() -> new IllegalStateException("This ingredient does not exist [" + ingredient.name() + "]"))
                ).collect(Collectors.toList());

        DishEntity createdDish = new DishEntity(
                databaseServices.generateSequence(MenuEnum.SEQUENCE_TYPE.DISHES),
                StringUtils.capitalize(newDish.name()),
                recipe
        );
        dishRepository.save(createdDish);
        return newDish;
    }

    public void updateDish(Dish updatedDish) throws IllegalStateException {
        // Check if dish does exist
        if (dishRepository.findById(updatedDish.id()).isEmpty()) {
            throw new IllegalStateException("This dish does not exist");
        }
        // Get Ingredients from database
        // In case one of them does not exist, an error is raised
        var recipe = updatedDish.recipe().stream()
                .map(
                    ingredient -> ingredientRepository.findById(ingredient.id())
                        .orElseThrow(IllegalStateException::new)
                ).collect(Collectors.toList());
        DishEntity newDish = dishMapper.toEntity(updatedDish);
        dishRepository.save(newDish);
    }

    public void deleteDish(Dish dishToDelete) throws IllegalStateException {
        // Check if dish does exist
        DishEntity storedDish = dishRepository.findById(dishToDelete.id())
                .orElseThrow(() -> new IllegalStateException("This dish does not exist"));
        // Check if dish is not used in any menu
        if (menuRepository.findMenuEntitiesByLunchMealsContainingOrDinnerMealsContaining(Stream.of(storedDish).toList()).isEmpty()) {
            dishRepository.deleteById(storedDish.id());
        } else {
            throw new IllegalStateException("This dish is planned and can not be deleted");
        }
    }
}
