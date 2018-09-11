package be.sandervl.kranzenzo.repository;

import be.sandervl.kranzenzo.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository <Authority, String> {
}
