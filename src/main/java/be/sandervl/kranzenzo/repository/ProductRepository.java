package be.sandervl.kranzenzo.repository;

import be.sandervl.kranzenzo.domain.Product;
import be.sandervl.kranzenzo.domain.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository <Product, Long>, QuerydslPredicateExecutor <Product>, QuerydslBinderCustomizer <QProduct> {

    @Query(value = "select distinct product from Product product left join fetch product.tags  left join fetch product.images",
        countQuery = "select count(distinct product) from Product product")
    Page <Product> findAllWithEagerRelationships( Pageable pageable );

    @Query(value = "select distinct product from Product product left join fetch product.tags left join fetch product.images")
    List <Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.tags left join fetch product.images where product.id =:id")
    Optional <Product> findOneWithEagerRelationships( @Param("id") Long id );

    @Query(value = "select distinct product from Product product left join fetch product.tags left join fetch product.images where product.isActive = :isActive",
        countQuery = "select count(distinct product) from Product product where product.isActive = :isActive")
    Page <Product> findAllByIsActive( @Param("isActive") boolean isActive, Pageable pageable );

    default void customize( QuerydslBindings bindings, QProduct product ) {
        bindings.bind( product.name ).first( StringExpression::startsWithIgnoreCase );
        bindings.bind( product.tags ).first( ( tags, value ) -> value.stream()
                                                                     .map( tags::contains )
                                                                     .reduce( BooleanExpression::or )
                                                                     .orElse( Expressions.FALSE ) );
    }

}
