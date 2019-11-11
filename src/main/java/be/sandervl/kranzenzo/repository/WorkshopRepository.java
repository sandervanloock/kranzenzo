package be.sandervl.kranzenzo.repository;
import be.sandervl.kranzenzo.domain.Workshop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Workshop entity.
 */
@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

    @Query(value = "select distinct workshop from Workshop workshop left join fetch workshop.tags",
        countQuery = "select count(distinct workshop) from Workshop workshop")
    Page<Workshop> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct workshop from Workshop workshop left join fetch workshop.tags")
    List<Workshop> findAllWithEagerRelationships();

    @Query("select workshop from Workshop workshop left join fetch workshop.tags where workshop.id =:id")
    Optional<Workshop> findOneWithEagerRelationships(@Param("id") Long id);

}
