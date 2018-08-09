package be.sandervl.kransenzo.service.mapper;

import be.sandervl.kransenzo.domain.Workshop;
import be.sandervl.kransenzo.service.dto.WorkshopDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Workshop and its DTO WorkshopDTO.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface WorkshopMapper extends EntityMapper <WorkshopDTO, Workshop> {

    @Mapping(target = "dates", ignore = true)
    @Mapping(target = "images", ignore = true)
    Workshop toEntity( WorkshopDTO workshopDTO );

    default Workshop fromId( Long id ) {
        if ( id == null ) {
            return null;
        }
        Workshop workshop = new Workshop();
        workshop.setId( id );
        return workshop;
    }
}
