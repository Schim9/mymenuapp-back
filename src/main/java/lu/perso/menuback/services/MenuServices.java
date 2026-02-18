package lu.perso.menuback.services;

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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServices {

    private final IngredientRepository ingredientRepository;
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    public MenuServices(IngredientRepository ingredientRepository, DishRepository dishRepository,
                        MenuRepository menuRepository, MenuMapper menuMapper) {
        this.ingredientRepository = ingredientRepository;
        this.dishRepository = dishRepository;
        this.menuRepository = menuRepository;
        this.menuMapper = menuMapper;
    }

    public List<Menu> getAllMenus(LocalDate selectedDate) {

        LocalDate startDate = selectedDate.minus(7, ChronoUnit.DAYS);
        LocalDate endDate = selectedDate.plus(7, ChronoUnit.DAYS);
        // Get all menus for previous and coming days
        List<MenuEntity> menuList = menuRepository.findAll(startDate, endDate);
        // Init List to be mapped and sent
        List<MenuEntity> result = new ArrayList<>();
        LocalDate currentDate = startDate;
        do {
            LocalDate finalVariable = currentDate;
            result.add(menuList.stream()
                    // In case a relevant menu was found
                    .filter(menu -> menu.getDate().isEqual(finalVariable))
                    .findFirst()
                    // or else, init with en empty menu
                    .orElse(new MenuEntity(null, currentDate.getDayOfWeek().name().toUpperCase(Locale.ROOT), currentDate, List.of(), List.of())));
            currentDate = currentDate.plus(1, ChronoUnit.DAYS);
        } while (currentDate.isBefore(endDate));

        return result.stream()
                .map(element -> menuMapper.toView(element))
                .collect(Collectors.toList());
    }

    public Menu handleMenuUpdate(Menu newMenu) throws IllegalStateException {
        // Check if menu already exists
        if (menuRepository.findByDate(newMenu.date()).isPresent()) {
            return this.updateMenu(newMenu);
        } else {
            return this.createMenu(newMenu);
        }

    }

    public Menu createMenu(Menu newMenu) throws IllegalStateException {
        // Check if menu already exists
        if (menuRepository.findByDate(newMenu.date()).isPresent()) {
            throw new IllegalStateException("A menu already exists for " + newMenu.date());
        }
        var lunchMeals = newMenu.lunchMeals().stream()
                .map(this::toItemMenuEntity)
                .collect(Collectors.toList());
        var dinnerMeals = newMenu.dinnerMeals().stream()
                .map(this::toItemMenuEntity)
                .collect(Collectors.toList());

        MenuEntity createdMenu = new MenuEntity(
                null,
                StringUtils.capitalize(newMenu.name()),
                newMenu.date(),
                lunchMeals,
                dinnerMeals
        );
        menuRepository.save(createdMenu);
        return menuMapper.toView(createdMenu);
    }

    public Menu updateMenu(Menu updatedMenu) throws IllegalStateException {
        MenuEntity existingMenu = menuRepository.findByDate(updatedMenu.date())
                .orElseThrow(() -> new IllegalStateException("This menu does not exist"));

        List<MenuItemEntity> lunchMeals = updatedMenu.lunchMeals().stream()
                .map(this::toItemMenuEntity)
                .collect(Collectors.toList());
        List<MenuItemEntity> dinnerMeals = updatedMenu.dinnerMeals().stream()
                .map(this::toItemMenuEntity)
                .collect(Collectors.toList());

        existingMenu.setName(StringUtils.capitalize(updatedMenu.name()));
        existingMenu.setLunchMeals(lunchMeals);
        existingMenu.setDinnerMeals(dinnerMeals);
        menuRepository.save(existingMenu);
        return menuMapper.toView(existingMenu);
    }

    public void deleteMenu(Menu menuToDelete) throws IllegalStateException {
        MenuEntity existingMenu = menuRepository.findByDate(menuToDelete.date())
                .orElseThrow(() -> new IllegalStateException("This menu does not exist"));
        existingMenu.setLunchMeals(new ArrayList<>());
        existingMenu.setDinnerMeals(new ArrayList<>());
        menuRepository.save(existingMenu);
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
