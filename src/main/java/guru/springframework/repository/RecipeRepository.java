package guru.springframework.repository;

import guru.springframework.domain.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
