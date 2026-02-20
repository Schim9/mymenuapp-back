package lu.perso.menuback.data;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_item")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class MenuItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
