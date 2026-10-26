package lu.perso.menuback.repository;

import lu.perso.menuback.data.ShoppingItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingListRepository extends JpaRepository<ShoppingItemEntity, Long> {
    List<ShoppingItemEntity> findAllByNameIn(List<String> names);
}
