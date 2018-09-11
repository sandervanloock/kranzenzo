package be.sandervl.kranzenzo.repository;

import be.sandervl.kranzenzo.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {

    @Query(value = "select distinct product from Product product left join fetch product.tags  left join fetch product.images",
        countQuery = "select count(distinct product) from Product product")
    Page <Product> findAllWithEagerRelationships( Pageable pageable );

    @Query(value = "select distinct product from Product product left join fetch product.tags left join fetch product.images")
    List <Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.tags left join fetch product.images where product.id =:id")
    Optional <Product> findOneWithEagerRelationships( @Param("id") Long id );

    @Query("select distinct product from Product product left join fetch product.tags left join fetch product.images where product.isActive = :isActive")
    List<Product> findAllByIsActive( @Param("isActive") boolean isActive );

}
