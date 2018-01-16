package be.sandervl.kransenzo.repository;

import be.sandervl.kransenzo.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository <Authority, String> {
}
