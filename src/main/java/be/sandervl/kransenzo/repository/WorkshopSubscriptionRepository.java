package be.sandervl.kransenzo.repository;

import be.sandervl.kransenzo.domain.WorkshopSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkshopSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkshopSubscriptionRepository extends JpaRepository <WorkshopSubscription, Long> {

}
