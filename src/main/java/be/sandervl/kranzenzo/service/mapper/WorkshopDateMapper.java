package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.*;
import be.sandervl.kranzenzo.service.dto.WorkshopDateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkshopDate} and its DTO {@link WorkshopDateDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkshopMapper.class})
public interface WorkshopDateMapper extends EntityMapper<WorkshopDateDTO, WorkshopDate> {

    @Mapping(source = "workshop.id", target = "workshopId")
    @Mapping(source = "workshop.name", target = "workshopName")
    WorkshopDateDTO toDto(WorkshopDate workshopDate);

    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "removeSubscriptions", ignore = true)
    @Mapping(source = "workshopId", target = "workshop")
    WorkshopDate toEntity(WorkshopDateDTO workshopDateDTO);

    default WorkshopDate fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkshopDate workshopDate = new WorkshopDate();
        workshopDate.setId(id);
        return workshopDate;
    }
}
