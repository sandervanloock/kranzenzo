package be.sandervl.kranzenzo.repository;

import be.sandervl.kranzenzo.domain.Workshop;
import be.sandervl.kranzenzo.domain.WorkshopDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Workshop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkshopRepository extends JpaRepository <Workshop, Long> {

    @Query(value = "select distinct workshop from Workshop workshop left join fetch workshop.tags left join fetch workshop.images left join fetch workshop.dates",
        countQuery = "select count(distinct workshop) from Workshop workshop")
    Page <Workshop> findAllWithEagerRelationships( Pageable pageable );

    @Query(value = "select distinct workshop from Workshop workshop left join fetch workshop.tags left join fetch workshop.images left join fetch workshop.dates")
    List <Workshop> findAllWithEagerRelationships();

    @Query(value = "select distinct workshop from Workshop workshop left join fetch workshop.tags left join fetch workshop.images left join fetch workshop.dates where workshop.showOnHomepage = 1 and workshop.isActive=1")
    List <Workshop> findByShowOnHomepageAndIsActive();

    @Query("select workshop from Workshop workshop left join fetch workshop.tags left join fetch workshop.images left join fetch workshop.dates where workshop.id =:id")
    Optional <Workshop> findOneWithEagerRelationships( @Param("id") Long id );

    Workshop findByDatesIsContaining( WorkshopDate workshop );
}
