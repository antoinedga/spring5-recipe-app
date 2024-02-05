package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repository.CategoryRepository;
import guru.springframework.repository.RecipeRepository;
import guru.springframework.repository.UnitOfMeasureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class Bootstrap implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository measureRepository;
    private final CategoryRepository categoryRepository;
    public Bootstrap(RecipeRepository recipeRepository, UnitOfMeasureRepository measureRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.measureRepository = measureRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<UnitOfMeasure> unitOfMeasures = (List<UnitOfMeasure>) measureRepository.findAll();

        Category italian = new Category();
        italian.setDescription("american");
        categoryRepository.save(italian);
        Recipe pasta = new Recipe();
        pasta.setDescription("pasta_with_spinach_artichokes_and_ricotta");
        pasta.setCookTime(60);
        pasta.setPrepTime(120);
        pasta.setDifficulty(Difficulty.HARD);
        pasta.setCategories(Collections.singleton(italian));
        italian.setRecipes(Collections.singleton(pasta));

        Ingredient oil = new Ingredient();
        oil.setDescription("olive oil");
        oil.setRecipe(pasta);
        oil.setAmount(BigDecimal.valueOf(10));
        oil.setUnitOfMeasure(unitOfMeasures.get(0));

        Ingredient panko = new Ingredient();
        panko.setDescription("panko");
        panko.setRecipe(pasta);
        panko.setAmount(BigDecimal.valueOf(5));
        panko.setUnitOfMeasure(unitOfMeasures.get(1));

        Ingredient ricotta = new Ingredient();
        ricotta.setDescription("ricotta cheese");
        ricotta.setRecipe(pasta);
        ricotta.setAmount(BigDecimal.valueOf(50));
        ricotta.setUnitOfMeasure(unitOfMeasures.get(2));

        pasta.setIngredients(new HashSet<>());
        pasta.getIngredients().addAll(Arrays.asList(new Ingredient[]{ricotta, panko, oil}));

        Notes pastaNote = new Notes();
        pastaNote.setRecipeNotes("its a good");

        // bidirectional relationship
        pastaNote.setRecipe(pasta);
        pasta.setNotes(pastaNote);

        recipeRepository.save(pasta);
        System.out.println("loaded recipe");
    }
}
