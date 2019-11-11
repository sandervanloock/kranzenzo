package be.sandervl.kranzenzo.repository;

import be.sandervl.kranzenzo.domain.HomepageSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the homepage settings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HomepageSettingsRepository extends JpaRepository <HomepageSettings, Long> {

}
