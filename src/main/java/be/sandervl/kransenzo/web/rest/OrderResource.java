package be.sandervl.kransenzo.web.rest;

import be.sandervl.kransenzo.service.OrderService;
import be.sandervl.kransenzo.service.dto.OrderDTO;
import be.sandervl.kransenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kransenzo.web.rest.util.HeaderUtil;
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
 * REST controller for managing Order.
 */
@RestController
@RequestMapping("/api")
public class OrderResource
{

    private final Logger log = LoggerFactory.getLogger( OrderResource.class );

    private static final String ENTITY_NAME = "order";

    private final OrderService orderService;

    public OrderResource( OrderService orderService ) {
        this.orderService = orderService;
    }

    /**
     * POST  /orders : Create a new order.
     *
     * @param orderDTO the orderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderDTO, or with status 400 (Bad Request) if the order has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orders")
    @Timed
    public ResponseEntity <OrderDTO> createOrder( @Valid @RequestBody OrderDTO orderDTO ) throws URISyntaxException {
        log.debug( "REST request to save Order : {}", orderDTO );
        if ( orderDTO.getId() != null ) {
            throw new BadRequestAlertException( "A new order cannot already have an ID", ENTITY_NAME, "idexists" );
        }
        OrderDTO result = orderService.create( orderDTO );
        return ResponseEntity.created( new URI( "/api/orders/" + result.getId() ) )
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( result );
    }

    /**
     * PUT  /orders : Updates an existing order.
     *
     * @param orderDTO the orderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderDTO,
     * or with status 400 (Bad Request) if the orderDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orders")
    @Timed
    public ResponseEntity <OrderDTO> updateOrder( @Valid @RequestBody OrderDTO orderDTO ) throws URISyntaxException {
        log.debug( "REST request to update Order : {}", orderDTO );
        if ( orderDTO.getId() == null ) {
            return createOrder( orderDTO );
        }
        OrderDTO result = orderService.save( orderDTO );
        return ResponseEntity.ok()
                             .headers( HeaderUtil.createEntityUpdateAlert( ENTITY_NAME, orderDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /orders : get all the orders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orders in body
     */
    @GetMapping("/orders")
    @Timed
    public List<OrderDTO> getAllOrders() {
        log.debug( "REST request to get all Orders" );
        return orderService.findAllOrderByUpdatedDesc();
    }

    /**
     * GET  /orders/:id : get the "id" order.
     *
     * @param id the id of the orderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/orders/{id}")
    @Timed
    public ResponseEntity <OrderDTO> getOrder( @PathVariable Long id ) {
        log.debug( "REST request to get Order : {}", id );
        OrderDTO orderDTO = orderService.findOne( id );
        return ResponseUtil.wrapOrNotFound( Optional.ofNullable( orderDTO ) );
    }

    /**
     * DELETE  /orders/:id : delete the "id" order.
     *
     * @param id the id of the orderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orders/{id}")
    @Timed
    public ResponseEntity <Void> deleteOrder( @PathVariable Long id ) {
        log.debug( "REST request to delete Order : {}", id );
        orderService.delete( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }

    /**
     * SEARCH  /_search/orders?query=:query : search for the order corresponding
     * to the query.
     *
     * @param query the query of the order search
     * @return the result of the search
     */
    @GetMapping("/_search/orders")
    @Timed
    public List <OrderDTO> searchOrders( @RequestParam String query ) {
        log.debug( "REST request to search Orders for query {}", query );
        return orderService.search( query );
    }

}
