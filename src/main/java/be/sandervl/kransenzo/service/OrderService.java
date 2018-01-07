package be.sandervl.kransenzo.service;

import be.sandervl.kransenzo.domain.Order;
import be.sandervl.kransenzo.domain.Product;
import be.sandervl.kransenzo.domain.User;
import be.sandervl.kransenzo.domain.enumeration.OrderState;
import be.sandervl.kransenzo.repository.CustomerRepository;
import be.sandervl.kransenzo.repository.OrderRepository;
import be.sandervl.kransenzo.repository.ProductRepository;
import be.sandervl.kransenzo.repository.search.OrderSearchRepository;
import be.sandervl.kransenzo.service.dto.OrderDTO;
import be.sandervl.kransenzo.service.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    private final ProductRepository productRepository;

    private final MailService mailService;

    private final CustomerRepository customerRepository;

    public OrderService( OrderRepository orderRepository,
                         OrderMapper orderMapper,
                         OrderSearchRepository orderSearchRepository,
                         ProductRepository productRepository,
                         MailService mailService,
                         CustomerRepository customerRepository ) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderSearchRepository = orderSearchRepository;
        this.productRepository = productRepository;
        this.mailService = mailService;
        this.customerRepository = customerRepository;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save
     * @return the persisted entity
     */
    public OrderDTO save( OrderDTO orderDTO ) {
        log.debug( "Request to save Order : {}", orderDTO );
        orderDTO.setUpdated( ZonedDateTime.now( ZoneId.systemDefault() ).withNano( 0 ) );
        Order order = orderMapper.toEntity( orderDTO );
        order = orderRepository.save( order );
        OrderDTO result = orderMapper.toDto( order );
        orderSearchRepository.save( order );
        updateProductVisibility( order );
        return result;
    }

    /**
     * Create a order.
     *
     * @param orderDTO the entity to create
     * @return the persisted entity
     */
    public OrderDTO create( OrderDTO orderDTO ) {
        log.debug( "Request to create Order : {}", orderDTO );
        orderDTO.setCreated( ZonedDateTime.now( ZoneId.systemDefault() ).withNano( 0 ) );
        orderDTO.setUpdated( ZonedDateTime.now( ZoneId.systemDefault() ).withNano( 0 ) );
        orderDTO.setState( OrderState.NEW );
        Order order = orderMapper.toEntity( orderDTO );
        order = orderRepository.save( order );
        orderSearchRepository.save( order );
        updateProductVisibility( order );
        if ( order.getCustomer() != null ) {
            //fetch customer eager so all information for sending the email is present on the order
            User user = customerRepository.getOne( order.getCustomer().getId() ).getUser();
            mailService.sendOrderCreationMails( order, user );
        }
        return orderMapper.toDto( order );
    }

    /**
     *  Get all the orders.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        log.debug( "Request to get all Orders" );
        return orderRepository.findAll().stream()
                              .map( orderMapper::toDto )
                              .collect( Collectors.toCollection( LinkedList::new ) );
    }

    /**
     *  Get one order by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OrderDTO findOne( Long id ) {
        log.debug( "Request to get Order : {}", id );
        Order order = orderRepository.findOne( id );
        return orderMapper.toDto( order );
    }

    /**
     *  Delete the  order by id.
     *
     *  @param id the id of the entity
     */
    public void delete( Long id ) {
        log.debug( "Request to delete Order : {}", id );
        updateProductVisibility( orderRepository.findOne( id ) );
        orderRepository.delete( id );
        orderSearchRepository.delete( id );
    }

    /**
     * Search for the order corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> search( String query ) {
        log.debug( "Request to search Orders for query {}", query );
        return StreamSupport
                .stream( orderSearchRepository.search( queryStringQuery( query ) ).spliterator(), false )
                .map( orderMapper::toDto )
                .collect( Collectors.toList() );
    }

    /*
        set product inactive when state is not CANCELLED, otherwise set it active again
     */
    private void updateProductVisibility( Order order ) {
        Product product = productRepository.findOneWithEagerRelationships( order.getProduct().getId() );
        product.setIsActive( order.getState().equals( OrderState.CANCELLED ) );
        product = productRepository.save( product );
        order.setProduct( product );
    }

    public List<OrderDTO> findAllOrderByUpdatedDesc() {
        return orderRepository.findAll( new Sort( Sort.Direction.DESC, "updated" ) ).stream()
                              .map( orderMapper::toDto )
                              .collect( Collectors.toCollection( LinkedList::new ) );

    }
}
