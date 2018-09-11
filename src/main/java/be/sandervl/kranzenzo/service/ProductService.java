package be.sandervl.kranzenzo.service;

import be.sandervl.kranzenzo.domain.Product;
import be.sandervl.kranzenzo.repository.ProductRepository;
import be.sandervl.kranzenzo.service.dto.ProductDTO;
import be.sandervl.kranzenzo.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger( ProductService.class );

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService( ProductRepository productRepository, ProductMapper productMapper ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save
     * @return the persisted entity
     */
    public ProductDTO save( ProductDTO productDTO ) {
        log.debug( "Request to save Product : {}", productDTO );
        Product product = productMapper.toEntity( productDTO );
        product = productRepository.save( product );
        return productMapper.toDto( product );
    }

    /**
     * Get all the products.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List <ProductDTO> findAll() {
        log.debug( "Request to get all Products" );
        return productRepository.findAllWithEagerRelationships().stream()
                                .map( productMapper::toDto )
                                .collect( Collectors.toCollection( LinkedList::new ) );
    }

    /**
     * Get all the Product with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page <ProductDTO> findAllWithEagerRelationships( Pageable pageable ) {
        return productRepository.findAllWithEagerRelationships( pageable ).map( productMapper::toDto );
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional <ProductDTO> findOne( Long id ) {
        log.debug( "Request to get Product : {}", id );
        return productRepository.findOneWithEagerRelationships( id )
                                .map( productMapper::toDto );
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity
     */
    public void delete( Long id ) {
        log.debug( "Request to delete Product : {}", id );
        productRepository.deleteById( id );
    }
}
