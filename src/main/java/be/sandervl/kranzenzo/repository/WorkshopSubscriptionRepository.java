package be.sandervl.kranzenzo.repository;

import be.sandervl.kranzenzo.domain.WorkshopDate;
import be.sandervl.kranzenzo.domain.WorkshopSubscription;
import be.sandervl.kranzenzo.domain.enumeration.SubscriptionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the WorkshopSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkshopSubscriptionRepository extends JpaRepository <WorkshopSubscription, Long> {
    int countByWorkshopAndState( WorkshopDate workshop, SubscriptionState state );

    List <WorkshopSubscription> findByWorkshopId( Long workshopDateId );
}
