package guru.springframework.service;

import guru.springframework.domain.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
public class ImageService {

    private final RecipeService recipeService;

    public ImageService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public void saveImageFile(Long recipeId, MultipartFile file) throws IOException {
        Recipe recipe = recipeService.findById(recipeId);
        Byte[] imgBytes = new Byte[file.getBytes().length];
        log.debug("Received File");
        int i =0;
        for (byte b: file.getBytes()) {
            imgBytes[i++] = b;
        }

        recipe.setImage(imgBytes);
        Recipe res = recipeService.saveRecipe(recipe);
        log.info(res.toString());
    }

}
