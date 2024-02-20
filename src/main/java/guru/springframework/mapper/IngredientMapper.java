package guru.springframework.mapper;


import guru.springframework.command.IngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {NotesMapper.class, UnitOfMeasurementMapper.class})
public interface IngredientMapper {

    @Mapping(target = "recipe", ignore = true)
    Ingredient commandToEntity(IngredientCommand ingredientCommand);

    @Mapping(target="recipeId", source = "ingredient.recipe.id")
    IngredientCommand entityToCommand(Ingredient ingredient);


    public default Long getRecipeIdInIngredient(Recipe recipe) {
        return recipe.getId();
    }
}
