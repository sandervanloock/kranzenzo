package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.*;
import be.sandervl.kranzenzo.service.dto.WorkshopSubscriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkshopSubscription} and its DTO {@link WorkshopSubscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkshopDateMapper.class})
public interface WorkshopSubscriptionMapper extends EntityMapper<WorkshopSubscriptionDTO, WorkshopSubscription> {

    @Mapping(source = "workshop.id", target = "workshopId")
    @Mapping(source = "workshop.date", target = "workshopDate")
    WorkshopSubscriptionDTO toDto(WorkshopSubscription workshopSubscription);

    @Mapping(source = "workshopId", target = "workshop")
    WorkshopSubscription toEntity(WorkshopSubscriptionDTO workshopSubscriptionDTO);

    default WorkshopSubscription fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkshopSubscription workshopSubscription = new WorkshopSubscription();
        workshopSubscription.setId(id);
        return workshopSubscription;
    }
}
