package be.sandervl.kransenzo.service.mapper;

import be.sandervl.kransenzo.domain.WorkshopSubscription;
import be.sandervl.kransenzo.service.dto.WorkshopSubscriptionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity WorkshopSubscription and its DTO WorkshopSubscriptionDTO.
 */
@Mapper(componentModel = "spring", uses = {WorkshopDateMapper.class})
public interface WorkshopSubscriptionMapper extends EntityMapper <WorkshopSubscriptionDTO, WorkshopSubscription> {

    @Mapping(source = "workshop.id", target = "workshopId")
    @Mapping(source = "workshop.date", target = "workshopDate")
    WorkshopSubscriptionDTO toDto( WorkshopSubscription workshopSubscription );

    @Mapping(source = "workshopId", target = "workshop")
    WorkshopSubscription toEntity( WorkshopSubscriptionDTO workshopSubscriptionDTO );

    default WorkshopSubscription fromId( Long id ) {
        if ( id == null ) {
            return null;
        }
        WorkshopSubscription workshopSubscription = new WorkshopSubscription();
        workshopSubscription.setId( id );
        return workshopSubscription;
    }
}
