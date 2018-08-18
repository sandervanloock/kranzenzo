package be.sandervl.kransenzo.repository;

import be.sandervl.kransenzo.domain.WorkshopDate;
import be.sandervl.kransenzo.domain.WorkshopSubscription;
import be.sandervl.kransenzo.domain.enumeration.SubscriptionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkshopSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkshopSubscriptionRepository extends JpaRepository <WorkshopSubscription, Long> {
    int countByWorkshopAndState( WorkshopDate workshop, SubscriptionState state );

    List <WorkshopSubscription> findByWorkshopId( Long workshopDateId );
}
