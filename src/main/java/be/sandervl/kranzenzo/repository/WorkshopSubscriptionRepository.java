package be.sandervl.kranzenzo.repository;
import be.sandervl.kranzenzo.domain.WorkshopSubscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WorkshopSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkshopSubscriptionRepository extends JpaRepository<WorkshopSubscription, Long> {

}
