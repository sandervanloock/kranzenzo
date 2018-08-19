package be.sandervl.kransenzo.repository;

import be.sandervl.kransenzo.domain.WorkshopDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkshopDate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkshopDateRepository extends JpaRepository <WorkshopDate, Long> {

}
