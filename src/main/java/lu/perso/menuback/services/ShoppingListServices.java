package lu.perso.menuback.services;

import lu.perso.menuback.data.ShoppingItemEntity;
import lu.perso.menuback.repository.ShoppingListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShoppingListServices {

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListServices(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @Transactional(readOnly = true)
    public List<String> getItems() {
        return shoppingListRepository.findAll()
                .stream()
                .map(ShoppingItemEntity::getName)
                .toList();
    }

    @Transactional
    public void addItems(List<String> names) {
        Set<String> existing = shoppingListRepository.findAllByNameIn(names)
                .stream()
                .map(ShoppingItemEntity::getName)
                .collect(Collectors.toSet());
        List<ShoppingItemEntity> toSave = names.stream()
                .filter(name -> !existing.contains(name))
                .map(ShoppingItemEntity::new)
                .toList();
        shoppingListRepository.saveAll(toSave);
    }

    @Transactional
    public List<String> consumeItems() {
        List<String> items = shoppingListRepository.findAll()
                .stream()
                .map(ShoppingItemEntity::getName)
                .toList();
        shoppingListRepository.deleteAll();
        return items;
    }
}
