package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.*;
import be.sandervl.kranzenzo.service.dto.WorkshopDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Workshop} and its DTO {@link WorkshopDTO}.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface WorkshopMapper extends EntityMapper<WorkshopDTO, Workshop> {


    @Mapping(target = "dates", ignore = true)
    @Mapping(target = "removeDates", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "removeImages", ignore = true)
    @Mapping(target = "removeTags", ignore = true)
    Workshop toEntity(WorkshopDTO workshopDTO);

    default Workshop fromId(Long id) {
        if (id == null) {
            return null;
        }
        Workshop workshop = new Workshop();
        workshop.setId(id);
        return workshop;
    }
}
