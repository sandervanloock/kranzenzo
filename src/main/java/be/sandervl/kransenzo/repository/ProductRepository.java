package be.sandervl.kransenzo.repository;

import be.sandervl.kransenzo.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
    
    @Query("select distinct product from Product product left join fetch product.tags left join fetch product.images")
    List<Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.tags left join fetch product.images where product.id =:id")
    Product findOneWithEagerRelationships( @Param("id") Long id );

    @Query("select distinct product from Product product left join fetch product.tags left join fetch product.images where product.isActive = :isActive")
    List<Product> findAllByIsActive( @Param("isActive") boolean isActive );
}
