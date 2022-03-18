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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

    @Autowired
    private MongoTemplate mongoTemplate;


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
                    .filter(menu -> menu.date().isEqual(finalVariable))
                    .findFirst()
                    // or else, init with en empty menu
                    .orElse(new MenuEntity(currentDate.getDayOfWeek().name().toUpperCase(Locale.ROOT), currentDate, List.of(), List.of())));
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
                StringUtils.capitalize(newMenu.name()),
                newMenu.date(),
                lunchMeals,
                dinnerMeals
        );
        menuRepository.save(createdMenu);
        return menuMapper.toView(createdMenu);
    }

    public Menu updateMenu(Menu updatedMenu) throws IllegalStateException {
        Optional<MenuEntity> byDate = menuRepository.findByDate(updatedMenu.date());
        // Check if menu does exist
        if (byDate.isEmpty()) {
            throw new IllegalStateException("This menu does not exist");
        }
        byDate.ifPresent(menu -> {
            // In case one of them does not exist, an error is raised
            //noinspection ResultOfMethodCallIgnored
            Stream.concat(
                            updatedMenu.lunchMeals().stream(),
                            updatedMenu.dinnerMeals().stream())
                    .map(this::toItemMenuEntity)
                    .collect(Collectors.toList());
            MenuEntity newMenu = menuMapper.toEntity(updatedMenu);

            Query query = new Query();
            query.addCriteria(Criteria.where("date").is(menu.date()));
            List<MenuItemEntity> lunchMeals = updatedMenu.lunchMeals().stream()
                    .map(this::toItemMenuEntity)
                    .collect(Collectors.toList());
            List<MenuItemEntity> dinnerMeals = updatedMenu.dinnerMeals().stream()
                    .map(this::toItemMenuEntity)
                    .collect(Collectors.toList());

            Update update = new Update();
            update.set("lunchMeals", lunchMeals);
            update.set("dinnerMeals", dinnerMeals);
            mongoTemplate.updateFirst(query, update, MenuEntity.class);
        });
        return updatedMenu;
    }

    // TODO: Do not delete, juste clean
    // Keep an empty records
    public void deleteMenu(Menu menuToDelete) throws IllegalStateException {
        throw new IllegalStateException("Not implemented");
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
