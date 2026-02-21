package lu.perso.menuback.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lu.perso.menuback.constant.MenuEnum.UNIT;
import lu.perso.menuback.models.Dish;
import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.models.MenuItem;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemDeserializer extends StdDeserializer<MenuItem> {

    public MenuItemDeserializer() {
        this(null); 
    } 

    public MenuItemDeserializer(Class<?> vc) {
        super(vc); 
    }

    @Override
    public MenuItem deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        long id = node.has("id") ? (node.get("id")).longValue() : 0L;
        String itemName = node.has("name") ? (node.get("name")).asText() : "";
        if (node.has("ingredients")) {
            // In case the JsonNode is a Dish
            List<Long> ingredients = new ArrayList<>();
            ArrayNode jsonRecipe = (ArrayNode) node.get("ingredients");
            jsonRecipe.elements().forEachRemaining(jsonNode -> ingredients.add(jsonNode.longValue()));
            return new Dish(id, itemName, ingredients);
        } else {
            // In case the JsonNode is an ingredient
            long sectionId = node.has("sectionId") ? (node.get("sectionId")).longValue() : 0L;
            UNIT unit = Optional.ofNullable(node.get("unit"))
                    .filter(enumValue -> !enumValue.isNull() && !enumValue.asText().isEmpty() && !enumValue.asText().equals("null"))
                    .map(enumValue -> UNIT.valueOf(enumValue.asText()))
                    .orElse(UNIT.PIECE);
            Boolean isDish = node.has("isDish") && !node.get("isDish").isNull() ? node.get("isDish").booleanValue() : null;

            return new Ingredient(id, itemName, sectionId, unit, isDish);
        }
    }
}