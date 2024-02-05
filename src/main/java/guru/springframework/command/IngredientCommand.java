package guru.springframework.command;

import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
public class IngredientCommand {

    private Long id;
    private String description;
    private BigDecimal amount;
    private Recipe recipe;
    private UnitOfMeasure unitOfMeasure;


}
