package lu.perso.menuback.services;

import lu.perso.menuback.data.DishEntity;
import lu.perso.menuback.data.IngredientEntity;
import lu.perso.menuback.data.MenuItemEntity;
import lu.perso.menuback.mappers.DishMapper;
import lu.perso.menuback.models.Dish;
import lu.perso.menuback.repository.DishRepository;
import lu.perso.menuback.repository.IngredientRepository;
import lu.perso.menuback.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DishServices {

    private final DishMapper dishMapper;
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    public DishServices(DishMapper dishMapper, MenuRepository menuRepository,
                        DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishMapper = dishMapper;
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional(readOnly = true)
    public List<Dish> getAllDishes() {
        List<DishEntity> dishList = dishRepository.findAll();
        return dishList.stream()
                .map(element -> dishMapper.toView(element))
                .collect(Collectors.toList());
    }

    @Transactional
    public Dish createDish(Dish newDish) throws IllegalStateException {
        // Check if dish already exists
        if (!Objects.isNull(dishRepository.findByName(newDish.name()))) {
           throw new IllegalStateException(newDish.name() + " does already exist");
        }
        // Get Ingredients from database
        // In case one of them does not exist, an error is raised
        var ingredients = newDish.ingredients()
            .stream()
            .map(this::toIngredientEntity)
            .toList();

        DishEntity createdDish = new DishEntity(
                null,
                StringUtils.capitalize(newDish.name()),
                ingredients
        );
        dishRepository.save(createdDish);
        return dishMapper.toView(createdDish);
    }

    @Transactional
    public void updateDish(Long dishId, Dish updatedDish) throws IllegalStateException {
        // Check if dish does exist
        if (dishRepository.findById(dishId).isEmpty()) {
            throw new IllegalStateException("This dish does not exist");
        }
        // In case one of them does not exist, an error is raised
        List<IngredientEntity> ingredientEntities = updatedDish.ingredients().stream()
                .map(this::toIngredientEntity)
                .collect(Collectors.toList());
        dishRepository.save(new DishEntity(dishId, updatedDish.name(), ingredientEntities));
    }

    public void deleteDish(Long dishId) throws IllegalStateException {
        // Check if dish does exist
        DishEntity storedDish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalStateException("This dish does not exist"));
        // Check if dish is not used in any menu
        List<MenuItemEntity> elements = Stream.of((MenuItemEntity) storedDish).toList();
        if (!menuRepository.findMenus(elements, elements).isEmpty()) {
            throw new IllegalStateException("This dish is planned and can not be deleted");
        } else {
            dishRepository.deleteById(storedDish.getId());
        }
    }

    private IngredientEntity toIngredientEntity(Long ingredientId) throws IllegalStateException {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalStateException(ingredientId + " does not exist"));
    }
}
