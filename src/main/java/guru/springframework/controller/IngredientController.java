package guru.springframework.controller;


import guru.springframework.command.IngredientCommand;
import guru.springframework.command.RecipeCommand;
import guru.springframework.command.UnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.mapper.IngredientMapper;
import guru.springframework.mapper.RecipeMapper;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasurementService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {
    private final IngredientService ingredientService;
    private final IngredientMapper ingredientMapper;
    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;
    private final UnitOfMeasurementService unitOfMeasurementService;
    public IngredientController(IngredientService ingredientService, IngredientMapper ingredientMapper, RecipeService recipeService,
                                RecipeMapper recipeMapper, UnitOfMeasurementService unitOfMeasurementService) {
        this.ingredientService = ingredientService;
        this.ingredientMapper = ingredientMapper;
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
        this.unitOfMeasurementService = unitOfMeasurementService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug(String.format("List of Ingredients for Recipe %s", recipeId));
        Recipe recipe = recipeService.findById(Long.valueOf(recipeId));
        model.addAttribute("recipe", recipeMapper.recipeEntityToRecipeCommand(recipe));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdandId(Long.valueOf(recipeId), Long.valueOf(id)));
        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateIngredient(@PathVariable String recipeId,
                                   @PathVariable String id,
                                   Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdandId(Long.valueOf(recipeId), Long.valueOf(id)));
        model.addAttribute("uomList", unitOfMeasurementService.getListOfUnitOfMeasure() );
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newRecipeIngredient(@PathVariable String recipeId, Model model) {
        RecipeCommand recipeCommand = recipeMapper
                .recipeEntityToRecipeCommand(recipeService.findById(Long.valueOf(recipeId)));
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasurementService.getListOfUnitOfMeasure());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping
    @RequestMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdateIngredient(@ModelAttribute IngredientCommand ingredientCommand) {
        IngredientCommand saved = ingredientService.saveIngredientCommand(ingredientCommand);
        log.info("recipeId is " + saved.getRecipeId());
        return "redirect:/recipe/" + saved.getRecipeId() + "/ingredient/" + saved.getId() + "/show";
    }

    @DeleteMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {
        ingredientService.deleteById(Long.valueOf(recipeId), Long.valueOf(id));
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
