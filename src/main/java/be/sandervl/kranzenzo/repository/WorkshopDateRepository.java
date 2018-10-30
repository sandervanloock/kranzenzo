package be.sandervl.kranzenzo.repository;

import be.sandervl.kranzenzo.domain.WorkshopDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the WorkshopDate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkshopDateRepository extends JpaRepository <WorkshopDate, Long> {

}
