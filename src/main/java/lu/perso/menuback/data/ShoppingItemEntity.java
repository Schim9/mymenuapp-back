package lu.perso.menuback.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shopping_list")
public class ShoppingItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public ShoppingItemEntity() {}

    public ShoppingItemEntity(String name) {
        this.name = name;
    }

}
