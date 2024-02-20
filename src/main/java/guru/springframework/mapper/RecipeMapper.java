package guru.springframework.mapper;

import guru.springframework.command.RecipeCommand;
import guru.springframework.domain.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class, NotesMapper.class, UnitOfMeasurementMapper.class})
public interface RecipeMapper {
    Recipe recipeCommandToEntity(RecipeCommand recipeCommand);
    //@Mapping(target = "ingredients", ignore = true)
    RecipeCommand recipeEntityToRecipeCommand(Recipe recipe);
}
