package guru.springframework.mapper;

import guru.springframework.command.RecipeCommand;
import guru.springframework.domain.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class, NotesMapper.class})
public interface RecipeMapper {
    Recipe recipeCommandToEntity(RecipeCommand recipeCommand);
    RecipeCommand recipeEntityToRecipeCommand(Recipe recipe);
}
