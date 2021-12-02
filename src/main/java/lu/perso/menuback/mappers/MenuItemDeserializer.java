package lu.perso.menuback.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lu.perso.menuback.constant.MenuEnum.UNIT;
import lu.perso.menuback.models.Dish;
import lu.perso.menuback.models.Ingredient;
import lu.perso.menuback.models.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemDeserializer extends StdDeserializer<MenuItem> {

    public MenuItemDeserializer() {
        this(null); 
    } 

    public MenuItemDeserializer(Class<?> vc) {
        super(vc); 
    }

    @Autowired
    ObjectMapper jsonObjectMapper;

    @Override
    public MenuItem deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        long id = node.has("id") ? (node.get("id")).longValue() : 0L;
        String itemName = node.get("name").asText();
        if (node.has("recipe")) {
            // In case the JsonNode is a Dish
            ArrayNode jsonRecipe = (ArrayNode) node.get("recipe");
            List<Ingredient> recipe = new ArrayList<>();
            for (JsonNode element : jsonRecipe) {
                Ingredient ingredient = jsonObjectMapper.treeToValue(element, Ingredient.class);
                recipe.add(ingredient);
            }
            return new Dish(id, itemName, recipe);
        } else {
            // In case the JsonNode is an ingredient
            long sectionId = node.get("sectionId").longValue();
            UNIT unit = UNIT.valueOf(node.get("unit").asText());
            return new Ingredient(id, itemName, sectionId, unit);
        }
    }
}