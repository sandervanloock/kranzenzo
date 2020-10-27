package be.sandervl.kranzenzo.service;

import be.sandervl.kranzenzo.domain.Customer;
import be.sandervl.kranzenzo.domain.ProductOrder;
import be.sandervl.kranzenzo.domain.User;
import be.sandervl.kranzenzo.domain.enumeration.OrderState;
import be.sandervl.kranzenzo.repository.CustomerRepository;
import be.sandervl.kranzenzo.repository.ProductOrderRepository;
import be.sandervl.kranzenzo.repository.ProductRepository;
import be.sandervl.kranzenzo.service.dto.ProductOrderDTO;
import be.sandervl.kranzenzo.service.mapper.ProductOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.sandervl.kranzenzo.config.Constants.WORKING_ZONE_ID;

/**
 * Service Implementation for managing ProductOrder.
 */
@Service
@Transactional
public class ProductOrderService {

    private final Logger log = LoggerFactory.getLogger( ProductOrderService.class );

    private final ProductOrderRepository productOrderRepository;

    private final ProductOrderMapper productOrderMapper;

    private final MailService mailService;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    public ProductOrderService( ProductOrderRepository productOrderRepository, ProductOrderMapper productOrderMapper, MailService mailService, CustomerRepository customerRepository, ProductRepository productRepository ) {
        this.productOrderRepository = productOrderRepository;
        this.productOrderMapper = productOrderMapper;
        this.mailService = mailService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    /**
     * Save a productOrder.
     *
     * @param productOrderDTO the entity to save
     * @return the persisted entity
     */
    public ProductOrderDTO save( ProductOrderDTO productOrderDTO ) {
        log.debug( "Request to save ProductOrder : {}", productOrderDTO );
        ProductOrder productOrder = productOrderMapper.toEntity( productOrderDTO );
        OrderState newState = productOrder.getState();
        OrderState currentState = productOrderRepository.findById( productOrderDTO.getId() )
                                                        .map( ProductOrder::getState )
                                                        .orElse( OrderState.CANCELLED );
        if ( newState.equals( OrderState.PAYED ) && !currentState.equals( OrderState.PAYED ) ) {
            log.debug( "Fetch the corresponding date and workshop so the data available for the mails" );
            //fetch customer eager so all information for sending the email is present on the order
            Customer customer = customerRepository.getOne( productOrder.getCustomer().getId() );
            productRepository.findById( productOrder.getProduct().getId() ).ifPresent( productOrder::setProduct );
            mailService.sendOrderPayedCustomer( productOrder, customer.getUser() );
        }
        productOrder = productOrderRepository.save( productOrder );
        updateProductVisibility( productOrder );
        return productOrderMapper.toDto( productOrder );
    }

    public ProductOrderDTO create( ProductOrderDTO orderDTO ) {
        log.debug( "Request to create Order : {}", orderDTO );
        orderDTO.setCreated( ZonedDateTime.now( WORKING_ZONE_ID ).withNano( 0 ) );
        orderDTO.setUpdated( ZonedDateTime.now( WORKING_ZONE_ID ).withNano( 0 ) );
        orderDTO.setState( OrderState.NEW );
        ProductOrder order = productOrderMapper.toEntity( orderDTO );
        order = productOrderRepository.save( order );
        updateProductVisibility( order );
        if ( order.getCustomer() != null ) {
            //fetch customer eager so all information for sending the email is present on the order
            Customer customer = customerRepository.getOne( order.getCustomer().getId() );
            order.setDeliveryAddress( customer.getAddress() );
            User user = customer.getUser();
            order.setCustomer( customer );
            mailService.sendOrderCreationMails( order, user );
        }
        return productOrderMapper.toDto( order );
    }

    /**
     * Get all the productOrders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List <ProductOrderDTO> findAll() {
        log.debug( "Request to get all ProductOrders" );
        return productOrderRepository.findAll().stream()
                                     .map( productOrderMapper::toDto )
                                     .collect( Collectors.toCollection( LinkedList::new ) );
    }

    public List <ProductOrderDTO> findAllOrderByUpdatedDesc() {
        return productOrderRepository.findAll( new Sort( Sort.Direction.DESC, "updated" ) ).stream()
                                     .map( productOrderMapper::toDto )
                                     .collect( Collectors.toCollection( LinkedList::new ) );
    }

    /**
     * Get one productOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional <ProductOrderDTO> findOne( Long id ) {
        log.debug( "Request to get ProductOrder : {}", id );
        return productOrderRepository.findById( id )
                                     .map( productOrderMapper::toDto );
    }

    /**
     * Delete the productOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete( Long id ) {
        log.debug( "Request to delete ProductOrder : {}", id );
        productOrderRepository.deleteById( id );
    }

    /*
       set product inactive when state is not CANCELLED, otherwise set it active again
    */
    private void updateProductVisibility( ProductOrder order ) {
        productRepository.findOneWithEagerRelationships( order.getProduct().getId() )
                         .ifPresent( product -> {
                             if ( product.isIsResell() != null && product.isIsResell() ) {
                                 product.setIsActive( true );
                             }
                             else{
                                 product.setIsActive( order.getState().equals( OrderState.CANCELLED ) );
                             }
                             product = productRepository.save( product );
                             order.setProduct( product );
                         } );
    }
}
