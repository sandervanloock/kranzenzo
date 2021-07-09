package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.service.ProductService;
import be.sandervl.kranzenzo.service.dto.ProductDTO;
import be.sandervl.kranzenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kranzenzo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private static final String ENTITY_NAME = "product";
    private final Logger log = LoggerFactory.getLogger( ProductResource.class );
    private final ProductService productService;

    public ProductResource( ProductService productService ) {
        this.productService = productService;
    }

    /**
     * POST  /products : Create a new product.
     *
     * @param productDTO the productDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productDTO, or with status 400 (Bad Request) if the product has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/products")
    @Timed
    public ResponseEntity <ProductDTO> createProduct( @Valid @RequestBody ProductDTO productDTO ) throws URISyntaxException {
        log.debug( "REST request to save Product : {}", productDTO );
        if ( productDTO.getId() != null ) {
            throw new BadRequestAlertException( "A new product cannot already have an ID", ENTITY_NAME, "idexists" );
        }
        productDTO.setCreated( ZonedDateTime.now() );
        ProductDTO result = productService.save( productDTO );
        return ResponseEntity.created( new URI( "/api/products/" + result.getId() ) )
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( result );
    }

    /**
     * PUT  /products : Updates an existing product.
     *
     * @param productDTO the productDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productDTO,
     * or with status 400 (Bad Request) if the productDTO is not valid,
     * or with status 500 (Internal Server Error) if the productDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/products")
    @Timed
    public ResponseEntity <ProductDTO> updateProduct( @Valid @RequestBody ProductDTO productDTO ) throws URISyntaxException {
        log.debug( "REST request to update Product : {}", productDTO );
        if ( productDTO.getId() == null ) {
            throw new BadRequestAlertException( "Invalid id", ENTITY_NAME, "idnull" );
        }
        ProductDTO result = productService.save( productDTO );
        return ResponseEntity.ok()
                             .headers( HeaderUtil
                                 .createEntityUpdateAlert( ENTITY_NAME, productDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /products : get all the products.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("/products")
    @Timed
    public List <ProductDTO> getAllProducts( @RequestParam(required = false, defaultValue = "false") boolean eagerload,
                                             @RequestParam(required = false, name = "activeOnly") Boolean activeOnly,
                                             @RequestParam(required = false, name = "tag") Long tagId,
                                             Pageable page ) {
        log.debug( "REST request to get all Products" );
        return productService.findAll( activeOnly, tagId, page ).getContent();
    }

    @GetMapping("/products/search")
    @Timed
    public Page<ProductDTO> searchProducts(@RequestParam(required = false) String query,
                                           @RequestParam(required = false) Boolean isActive,
                                           @RequestParam(required = false) Integer tags,
                                           @PageableDefault(size = 10)
                                           @SortDefault.SortDefaults({
                                               @SortDefault(sort = "created", direction = Sort.Direction.DESC),
                                               @SortDefault(sort = "name", direction = Sort.Direction.ASC)
                                           }) Pageable customPage) {
        log.debug("REST request to get all Products");
        return productService.search(query, isActive, tags, customPage);
    }

    /**
     * GET  /products/:id : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productDTO, or with status 404 (Not Found)
     */
    @GetMapping("/products/{id}")
    @Timed
    public ResponseEntity <ProductDTO> getProduct( @PathVariable Long id ) {
        log.debug( "REST request to get Product : {}", id );
        Optional <ProductDTO> productDTO = productService.findOne( id );
        return ResponseUtil.wrapOrNotFound( productDTO );
    }

    /**
     * DELETE  /products/:id : delete the "id" product.
     *
     * @param id the id of the productDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/products/{id}")
    @Timed
    public ResponseEntity <Void> deleteProduct( @PathVariable Long id ) {
        log.debug( "REST request to delete Product : {}", id );
        productService.delete( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }
}
