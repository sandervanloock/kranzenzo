package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.service.ProductOrderService;
import be.sandervl.kranzenzo.service.ProductService;
import be.sandervl.kranzenzo.service.dto.ProductDTO;
import be.sandervl.kranzenzo.service.dto.ProductOrderDTO;
import be.sandervl.kranzenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kranzenzo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProductOrder.
 */
@RestController
@RequestMapping("/api")
public class ProductOrderResource {

    private static final String ENTITY_NAME = "productOrder";
    private final Logger log = LoggerFactory.getLogger( ProductOrderResource.class );
    private final ProductOrderService productOrderService;
    private final ProductService productService;

    public ProductOrderResource( ProductOrderService productOrderService, ProductService productService ) {
        this.productOrderService = productOrderService;
        this.productService = productService;
    }

    /**
     * POST  /product-orders : Create a new productOrder.
     *
     * @param productOrderDTO the productOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productOrderDTO, or with status 400 (Bad Request) if the productOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/product-orders")
    @Timed
    public ResponseEntity <ProductOrderDTO> createProductOrder( @Valid @RequestBody ProductOrderDTO productOrderDTO ) throws URISyntaxException {
        log.debug( "REST request to save ProductOrder : {}", productOrderDTO );
        if ( productOrderDTO.getId() != null ) {
            throw new BadRequestAlertException( "A new productOrder cannot already have an ID", ENTITY_NAME, "idexists" );
        }
        ProductDTO productDTO = productService.findOne( productOrderDTO.getProduct().getId() )
                                              .orElseThrow( () -> new BadRequestAlertException( "Product not found", ENTITY_NAME, "unknownId" ) );
        if ( Boolean.FALSE.equals( productDTO.isIsActive() ) ) {
            throw new BadRequestAlertException( "Product not active", ENTITY_NAME, "productNotActive" );
        }
        ProductOrderDTO result = productOrderService.create( productOrderDTO );
        return ResponseEntity.created( new URI( "/api/product-orders/" + result.getId() ) )
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( result );
    }

    /**
     * PUT  /product-orders : Updates an existing productOrder.
     *
     * @param productOrderDTO the productOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productOrderDTO,
     * or with status 400 (Bad Request) if the productOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the productOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/product-orders")
    @Timed
    public ResponseEntity <ProductOrderDTO> updateProductOrder( @Valid @RequestBody ProductOrderDTO productOrderDTO ) throws URISyntaxException {
        log.debug( "REST request to update ProductOrder : {}", productOrderDTO );
        if ( productOrderDTO.getId() == null ) {
            throw new BadRequestAlertException( "Invalid id", ENTITY_NAME, "idnull" );
        }
        ProductOrderDTO result = productOrderService.save( productOrderDTO );
        return ResponseEntity.ok()
                             .headers( HeaderUtil
                                 .createEntityUpdateAlert( ENTITY_NAME, productOrderDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /product-orders : get all the productOrders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productOrders in body
     */
    @GetMapping("/product-orders")
    @Timed
    public List <ProductOrderDTO> getAllProductOrders() {
        log.debug( "REST request to get all ProductOrders" );
        List <ProductOrderDTO> all = productOrderService.findAll();
        all.sort( ( a, b ) -> b.getCreated().compareTo( a.getCreated() ) );
        return all;
    }

    /**
     * ProductOrderResourceIntTest
     * GET  /product-orders/:id : get the "id" productOrder.
     *
     * @param id the id of the productOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/product-orders/{id}")
    @Timed
    public ResponseEntity <ProductOrderDTO> getProductOrder( @PathVariable Long id ) {
        log.debug( "REST request to get ProductOrder : {}", id );
        Optional <ProductOrderDTO> productOrderDTO = productOrderService.findOne( id );
        return ResponseUtil.wrapOrNotFound( productOrderDTO );
    }

    /**
     * DELETE  /product-orders/:id : delete the "id" productOrder.
     *
     * @param id the id of the productOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/product-orders/{id}")
    @Timed
    public ResponseEntity <Void> deleteProductOrder( @PathVariable Long id ) {
        log.debug( "REST request to delete ProductOrder : {}", id );
        productOrderService.delete( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }
}
