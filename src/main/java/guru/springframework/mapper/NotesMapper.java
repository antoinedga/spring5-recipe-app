package guru.springframework.mapper;

import guru.springframework.command.NotesCommand;
import guru.springframework.domain.Notes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotesMapper {
    Notes commandToEntity(NotesCommand notesCommand);
    NotesCommand entityToCommand(Notes notes);
}
