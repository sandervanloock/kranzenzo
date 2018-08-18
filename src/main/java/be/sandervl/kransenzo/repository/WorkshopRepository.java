package be.sandervl.kransenzo.repository;

import be.sandervl.kransenzo.domain.Workshop;
import be.sandervl.kransenzo.domain.WorkshopDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Workshop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkshopRepository extends JpaRepository <Workshop, Long> {
    @Query("select distinct workshop from Workshop workshop left join fetch workshop.tags left join fetch workshop.images left join fetch workshop.dates")
    List <Workshop> findAllWithEagerRelationships();

    @Query("select workshop from Workshop workshop left join fetch workshop.tags left join fetch workshop.images left join fetch workshop.dates where workshop.id =:id")
    Workshop findOneWithEagerRelationships( @Param("id") Long id );

    Workshop findByDatesIsContaining( WorkshopDate workshop );
}
