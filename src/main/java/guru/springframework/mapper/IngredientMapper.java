package guru.springframework.mapper;


import guru.springframework.command.IngredientCommand;
import guru.springframework.domain.Ingredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredient commandToEntity(IngredientCommand ingredientCommand);
    IngredientCommand entityToCommand(Ingredient ingredient);
}
