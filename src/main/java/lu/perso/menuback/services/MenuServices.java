package lu.perso.menuback.services;

import lu.perso.menuback.constant.MenuEnum;
import lu.perso.menuback.data.MenuEntity;
import lu.perso.menuback.data.MenuItemEntity;
import lu.perso.menuback.mappers.MenuMapper;
import lu.perso.menuback.models.Dish;
import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.models.Menu;
import lu.perso.menuback.models.MenuItem;
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
public class MenuServices {

    @Autowired
    DatabaseServices databaseServices;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    DishRepository dishRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuMapper menuMapper;

    public List<Menu> getAllMenus() {
        List<MenuEntity> menuList = menuRepository.findAll();
        return menuList.stream()
                .map(element -> menuMapper.toView(element))
                .collect(Collectors.toList());
    }

    public Menu createMenu(Menu newMenu) throws IllegalStateException {
        // Check if menu already exists
        if (!Objects.isNull(menuRepository.findByName(newMenu.name()))) {
            throw new IllegalStateException(newMenu.name() + " does already exist");
        }
        var lunchMeals = newMenu.lunchMeals().stream()
                .map(this::toItemMenuEntity)
                .collect(Collectors.toList());
        var dinnerMeals = newMenu.dinnerMeals().stream()
                .map(this::toItemMenuEntity)
                .collect(Collectors.toList());

        MenuEntity createdMenu = new MenuEntity(
                databaseServices.generateSequence(MenuEnum.SEQUENCE_TYPE.MENUS),
                StringUtils.capitalize(newMenu.name()),
                lunchMeals,
                dinnerMeals
        );
        menuRepository.save(createdMenu);
        return newMenu;
    }

    public void updateMenu(Menu updatedMenu) throws IllegalStateException {
        // Check if menu does exist
        if (menuRepository.findById(updatedMenu.id()).isEmpty()) {
            throw new IllegalStateException("This menu does not exist");
        }
        // In case one of them does not exist, an error is raised
        //noinspection ResultOfMethodCallIgnored
        Stream.concat(
                        updatedMenu.lunchMeals().stream(),
                        updatedMenu.dinnerMeals().stream())
                .map(this::toItemMenuEntity)
                .collect(Collectors.toList());
        MenuEntity newMenu = menuMapper.toEntity(updatedMenu);
        menuRepository.save(newMenu);
    }

    public void deleteMenu(Menu menuToDelete) throws IllegalStateException {
        // Check if menuToDelete does exist
        MenuEntity storedMenu = menuRepository.findById(menuToDelete.id())
                .orElseThrow(() -> new IllegalStateException("This menu does not exist"));
        menuRepository.deleteById(storedMenu.id());
    }

    private MenuItemEntity toItemMenuEntity(MenuItem menuItem) throws IllegalStateException {
        return switch (menuItem) {
            case Ingredient ingredient -> ingredientRepository
                    .findById(ingredient.id())
                    .orElseThrow(() -> new IllegalStateException(ingredient.name() + " does not exist"));
            case Dish dish -> dishRepository.findById(dish.id())
                    .orElseThrow(() -> new IllegalStateException(dish.name() + " does not exist"));
        };
    }

}
