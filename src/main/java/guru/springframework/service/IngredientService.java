package guru.springframework.service;


import guru.springframework.command.IngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.mapper.IngredientMapper;
import guru.springframework.repository.RecipeRepository;
import guru.springframework.repository.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class IngredientService {


    private final RecipeRepository recipeRepository;
    private final IngredientMapper ingredientMapper;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientService(RecipeRepository recipeRepository, IngredientMapper ingredientMapper, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientMapper = ingredientMapper;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    public IngredientCommand findByRecipeIdandId(Long recipeId, Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (!recipeOptional.isPresent()) {
            log.error("no recipe by that id");
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(id))
                .map(ingredientMapper::entityToCommand)
                .findFirst();

        if (!ingredientCommandOptional.isPresent()) {
            log.error("Error with finding ingredient within Recipe");
        }
        IngredientCommand ingredientCommand = ingredientCommandOptional.get();
        ingredientCommand.setRecipeId(recipeId);
        return ingredientCommandOptional.get();
    }

    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (!recipeOptional.isPresent()) {

            //todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                        .findById(command.getUnitOfMeasure().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientMapper.commandToEntity(command);
                recipe.getIngredients().add(ingredient);
                ingredient.setRecipe(recipe);

            }

            Recipe savedRecipe = recipeRepository.save(recipe);
            Optional<Ingredient> saveIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngs -> recipeIngs.getId().equals(command.getId()))
                    .findFirst();


            if (!saveIngredientOptional.isPresent()) {
                saveIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equalsIgnoreCase(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getUom().equalsIgnoreCase(command.getUnitOfMeasure().getUom()))
                        .findFirst();
            }

            return ingredientMapper.entityToCommand(saveIngredientOptional.get());
        }

    }

    public void deleteById(Long recipeId, Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isPresent()) {
            Optional<Ingredient> ingredientOptional = recipeOptional.get()
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(id))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                ingredientOptional.get().setRecipe(null);
                recipeOptional.get().getIngredients().remove(ingredientOptional.get());
            }
            recipeRepository.save(recipeOptional.get());
        }

    }
}