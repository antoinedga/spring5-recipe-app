package guru.springframework.controller;

import guru.springframework.command.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.exception.NotFoundException;
import guru.springframework.mapper.RecipeMapper;
import guru.springframework.service.ImageService;
import guru.springframework.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;


@Slf4j
@Controller
public class RecipeController {
    public static final String RECIPE = "recipe";
    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;
    private final ImageService imageService;
    public RecipeController(RecipeService recipeService, RecipeMapper recipeMapper, ImageService imageService) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
        this.imageService = imageService;
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

    @GetMapping("/recipe/{recipeId}/image")
    public String getImageUploadForm(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(recipeId)));
        return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String saveImageForRecipe(@PathVariable String recipeId, @RequestParam("imagefile")MultipartFile file) throws IOException {
        log.info("received image request");
        imageService.saveImageFile(Long.valueOf(recipeId), file);
        return "redirect:/recipe/" + recipeId + "/show";
    }

    @GetMapping("/recipe/{recipeId}/recipeimage")
    public void getImageForRecipe(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        log.info("got get Request For Image");
        Recipe recipe = recipeService.findById(Long.valueOf(recipeId));
        byte[] array = new byte[recipe.getImage().length];
        int i = 0;
        for (Byte wrapped: recipe.getImage()) {
            array[i++] = wrapped;
        }

        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(array);
        IOUtils.copy(is, response.getOutputStream());
    }


    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundPage(Exception exception) {
        log.error("ass and titties");
        ModelAndView modelAndView = new ModelAndView("404Error");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView numberFormatException(Exception exception) {
        log.error("ass and titties");
        ModelAndView modelAndView = new ModelAndView("404Error");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
}
