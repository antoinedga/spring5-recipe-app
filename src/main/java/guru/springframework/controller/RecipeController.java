package guru.springframework.controller;

import guru.springframework.command.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.mapper.RecipeMapper;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
public class RecipeController {
    public static final String RECIPE = "recipe";
    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;
    public RecipeController(RecipeService recipeService, RecipeMapper recipeMapper) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
    }
    @GetMapping({"/recipe/{id}/show"})
    public String showById(@PathVariable String id, Model model) {
        log.info("get request for " + id);
        model.addAttribute(RECIPE, recipeService.findById(Long.parseLong(id)));
        return "recipe/show";
    }

    @GetMapping
    @RequestMapping("/recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute(RECIPE, new RecipeCommand());
        return "recipe/recipeform";
    }

    @PostMapping
    @RequestMapping(name = "/recipe")
    public String save(@ModelAttribute RecipeCommand recipeCommand) {
        Recipe recipe = recipeMapper.recipeCommandToEntity(recipeCommand);
        recipe = recipeService.saveRecipe(recipe);
        return "redirect:/recipe/" + recipe.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute(RECIPE, recipeService.findById(Long.valueOf(id)));
        return "recipe/recipeform";
    }


    @RequestMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipe(Long.valueOf(id));
        return "redirect:/";
    }

}
