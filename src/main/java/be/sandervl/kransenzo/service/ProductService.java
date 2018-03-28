package be.sandervl.kransenzo.service;

import be.sandervl.kransenzo.domain.Product;
import be.sandervl.kransenzo.repository.ProductRepository;
import be.sandervl.kransenzo.repository.search.ProductSearchRepository;
import be.sandervl.kransenzo.service.dto.ProductDTO;
import be.sandervl.kransenzo.service.mapper.ProductMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductService{

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;

    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper,
                          ProductSearchRepository productSearchRepository){
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save
     * @return the persisted entity
     */
    public ProductDTO save(ProductDTO productDTO){
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        ProductDTO result = productMapper.toDto(product);
//        productSearchRepository.save(product);
        return result;
    }

    /**
     * Get all the products.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll(Boolean activeOnly, String tagName){
        log.debug( "Request to get all Products" );
        List<Product> products;
        if ( activeOnly != null ) {
            products = productRepository.findAllByIsActive( activeOnly );
        }
        else {
            products = productRepository.findAllWithEagerRelationships();
        }
        return products.stream()
                       .filter(p -> StringUtils.isBlank(tagName) ||
                           p.getTags().stream().anyMatch(t -> t.getName().equalsIgnoreCase(tagName)))
                       .map(productMapper::toDto)
                       .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProductDTO findOne(Long id){
        log.debug("Request to get Product : {}", id);
        Product product = productRepository.findOneWithEagerRelationships(id);
        return productMapper.toDto(product);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id){
        log.debug("Request to delete Product : {}", id);
        productRepository.delete(id);
//        productSearchRepository.delete(id);
    }

    /**
     * Search for the product corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> search(String query){
        log.debug("Request to search Products for query {}", query);
        return StreamSupport
            .stream(productSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(productMapper::toDto)
            .collect(Collectors.toList());
    }
}
