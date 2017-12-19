package be.sandervl.kransenzo.service;

import be.sandervl.kransenzo.domain.Order;
import be.sandervl.kransenzo.repository.OrderRepository;
import be.sandervl.kransenzo.repository.search.OrderSearchRepository;
import be.sandervl.kransenzo.service.dto.OrderDTO;
import be.sandervl.kransenzo.service.mapper.OrderMapper;
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
 * Service Implementation for managing Order.
 */
@Service
@Transactional
public class OrderService
{

    private final Logger log = LoggerFactory.getLogger( OrderService.class );

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OrderSearchRepository orderSearchRepository;

    public OrderService( OrderRepository orderRepository,
                         OrderMapper orderMapper,
                         OrderSearchRepository orderSearchRepository ) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderSearchRepository = orderSearchRepository;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save
     * @return the persisted entity
     */
    public OrderDTO save( OrderDTO orderDTO ) {
        log.debug( "Request to save Order : {}", orderDTO );
        Order order = orderMapper.toEntity( orderDTO );
        order = orderRepository.save( order );
        OrderDTO result = orderMapper.toDto( order );
        orderSearchRepository.save( order );
        return result;
    }

    /**
     * Get all the orders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        log.debug( "Request to get all Orders" );
        return orderRepository.findAll().stream()
                              .map( orderMapper::toDto )
                              .collect( Collectors.toCollection( LinkedList::new ) );
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrderDTO findOne( Long id ) {
        log.debug( "Request to get Order : {}", id );
        Order order = orderRepository.findOne( id );
        return orderMapper.toDto( order );
    }

    /**
     * Delete the  order by id.
     *
     * @param id the id of the entity
     */
    public void delete( Long id ) {
        log.debug( "Request to delete Order : {}", id );
        orderRepository.delete( id );
        orderSearchRepository.delete( id );
    }

    /**
     * Search for the order corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> search( String query ) {
        log.debug( "Request to search Orders for query {}", query );
        return StreamSupport
                .stream( orderSearchRepository.search( queryStringQuery( query ) ).spliterator(), false )
                .map( orderMapper::toDto )
                .collect( Collectors.toList() );
    }
}
