package guru.springframework.service;


import guru.springframework.command.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repository.UnitOfMeasureRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UnitOfMeasurementService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasurementService(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    public Set<UnitOfMeasureCommand> getListOfUnitOfMeasure() {
        return StreamSupport.stream(unitOfMeasureRepository.findAll().spliterator(), false)
                .map(unitOfMeasure -> {
                    UnitOfMeasureCommand u = new UnitOfMeasureCommand();
                    u.setId(unitOfMeasure.getId());
                    u.setUom(unitOfMeasure.getUom());
                    return u;
                })
                .collect(Collectors.toSet());
    }
}
