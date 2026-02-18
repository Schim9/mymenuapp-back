package lu.perso.menuback.data;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    LocalDate date;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "lunch_meals",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    List<MenuItemEntity> lunchMeals;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "dinner_meals",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    List<MenuItemEntity> dinnerMeals;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<MenuItemEntity> getLunchMeals() {
        return lunchMeals;
    }

    public void setLunchMeals(List<MenuItemEntity> lunchMeals) {
        this.lunchMeals = lunchMeals;
    }

    public List<MenuItemEntity> getDinnerMeals() {
        return dinnerMeals;
    }

    public void setDinnerMeals(List<MenuItemEntity> dinnerMeals) {
        this.dinnerMeals = dinnerMeals;
    }

    public MenuEntity() {
        this.lunchMeals = new ArrayList<>();
        this.dinnerMeals = new ArrayList<>();
    }

    public MenuEntity(Long id, String name,
                      LocalDate date, List<MenuItemEntity> lunchMeals, List<MenuItemEntity> dinnerMeals) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.lunchMeals = lunchMeals;
        this.dinnerMeals = dinnerMeals;
    }
}
