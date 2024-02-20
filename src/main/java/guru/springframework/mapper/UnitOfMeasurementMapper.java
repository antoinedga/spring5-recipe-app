package guru.springframework.mapper;


import guru.springframework.command.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitOfMeasurementMapper {

    UnitOfMeasureCommand entityToCommand(UnitOfMeasure unitOfMeasure);
    UnitOfMeasure commandToEntity(UnitOfMeasureCommand unitOfMeasureCommand);
}
