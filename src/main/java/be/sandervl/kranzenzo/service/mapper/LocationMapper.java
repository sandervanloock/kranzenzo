package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.Location;
import be.sandervl.kranzenzo.service.dto.LocationDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Location and its DTO LocationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocationMapper extends EntityMapper <LocationDTO, Location> {

    default Location fromId( Long id ) {
        if ( id == null ) {
            return null;
        }
        Location location = new Location();
        location.setId( id );
        return location;
    }
}
