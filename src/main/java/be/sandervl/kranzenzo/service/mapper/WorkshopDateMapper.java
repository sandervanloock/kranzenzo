package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.WorkshopDate;
import be.sandervl.kranzenzo.service.dto.WorkshopDateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity WorkshopDate and its DTO WorkshopDateDTO.
 */
@Mapper(componentModel = "spring", uses = {WorkshopMapper.class})
public interface WorkshopDateMapper extends EntityMapper <WorkshopDateDTO, WorkshopDate> {

    @Mapping(source = "workshop.id", target = "workshopId")
    @Mapping(source = "workshop.name", target = "workshopName")
    WorkshopDateDTO toDto( WorkshopDate workshopDate );

    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(source = "workshopId", target = "workshop")
    WorkshopDate toEntity( WorkshopDateDTO workshopDateDTO );

    default WorkshopDate fromId( Long id ) {
        if ( id == null ) {
            return null;
        }
        WorkshopDate workshopDate = new WorkshopDate();
        workshopDate.setId( id );
        return workshopDate;
    }
}
