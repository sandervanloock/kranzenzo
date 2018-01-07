package be.sandervl.kransenzo.web.rest;

import be.sandervl.kransenzo.KransenzoApp;
import be.sandervl.kransenzo.domain.Order;
import be.sandervl.kransenzo.domain.Product;
import be.sandervl.kransenzo.domain.enumeration.DeliveryType;
import be.sandervl.kransenzo.domain.enumeration.OrderState;
import be.sandervl.kransenzo.repository.OrderRepository;
import be.sandervl.kransenzo.repository.search.OrderSearchRepository;
import be.sandervl.kransenzo.service.OrderService;
import be.sandervl.kransenzo.service.dto.OrderDTO;
import be.sandervl.kransenzo.service.mapper.OrderMapper;
import be.sandervl.kransenzo.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static be.sandervl.kransenzo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the OrderResource REST controller.
 *
 * @see OrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KransenzoApp.class)
public class OrderResourceIntTest
{

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant( Instant.ofEpochMilli( 0L ),
                                                                                  ZoneOffset.UTC );
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now( ZoneId.systemDefault() ).withNano( 0 );

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant( Instant.ofEpochMilli( 0L ),
                                                                                  ZoneOffset.UTC );
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now( ZoneId.systemDefault() ).withNano( 0 );

    private static final OrderState DEFAULT_STATE = OrderState.NEW;
    private static final OrderState UPDATED_STATE = OrderState.PAYED;

    private static final DeliveryType DEFAULT_DELIVERY_TYPE = DeliveryType.DELIVERED;
    private static final DeliveryType UPDATED_DELIVERY_TYPE = DeliveryType.PICKUP;

    private static final Boolean DEFAULT_INCLUDE_BATTERIES = false;
    private static final Boolean UPDATED_INCLUDE_BATTERIES = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_DELIVERY_PRICE = 0F;
    private static final Float UPDATED_DELIVERY_PRICE = 1F;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSearchRepository orderSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity( EntityManager em ) {
        Order order = new Order()
                .created( DEFAULT_CREATED )
                .updated( DEFAULT_UPDATED )
                .state( DEFAULT_STATE )
                .deliveryType( DEFAULT_DELIVERY_TYPE )
                .includeBatteries( DEFAULT_INCLUDE_BATTERIES )
                .description( DEFAULT_DESCRIPTION )
                .deliveryPrice( DEFAULT_DELIVERY_PRICE );
        // Add required entity
        Product product = ProductResourceIntTest.createEntity( em );
        em.persist( product );
        em.flush();
        order.setProduct( product );
        return order;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks( this );
        OrderResource orderResource = new OrderResource( orderService );
        this.restOrderMockMvc = MockMvcBuilders.standaloneSetup( orderResource )
                                               .setCustomArgumentResolvers( pageableArgumentResolver )
                                               .setControllerAdvice( exceptionTranslator )
                                               .setMessageConverters( jacksonMessageConverter ).build();
    }

    @Before
    public void initTest() {
        orderSearchRepository.deleteAll();
        order = createEntity( em );
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto( order );
        restOrderMockMvc.perform( post( "/api/orders" )
                                          .contentType( TestUtil.APPLICATION_JSON_UTF8 )
                                          .content( TestUtil.convertObjectToJsonBytes( orderDTO ) ) )
                        .andExpect( status().isCreated() );

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat( orderList ).hasSize( databaseSizeBeforeCreate + 1 );
        Order testOrder = orderList.get( orderList.size() - 1 );
        assertThat( testOrder.getState() ).isEqualTo( DEFAULT_STATE );
        assertThat( testOrder.getDeliveryType() ).isEqualTo( DEFAULT_DELIVERY_TYPE );
        assertThat( testOrder.isIncludeBatteries() ).isEqualTo( DEFAULT_INCLUDE_BATTERIES );
        assertThat( testOrder.getDescription() ).isEqualTo( DEFAULT_DESCRIPTION );

        // Validate the Order in Elasticsearch
        Order orderEs = orderSearchRepository.findOne( testOrder.getId() );
        assertThat( orderEs ).isEqualToIgnoringGivenFields( testOrder, "updated", "created" );
    }

    @Test
    @Transactional
    public void createOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order with an existing ID
        order.setId( 1L );
        OrderDTO orderDTO = orderMapper.toDto( order );

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc.perform( post( "/api/orders" )
                                          .contentType( TestUtil.APPLICATION_JSON_UTF8 )
                                          .content( TestUtil.convertObjectToJsonBytes( orderDTO ) ) )
                        .andExpect( status().isBadRequest() );

        // Validate the Alice in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat( orderList ).hasSize( databaseSizeBeforeCreate );
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush( order );

        // Get all the orderList
        restOrderMockMvc.perform( get( "/api/orders?sort=id,desc" ) )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                        .andExpect( jsonPath( "$.[*].id" ).value( hasItem( order.getId().intValue() ) ) )
                        .andExpect( jsonPath( "$.[*].created" ).value( hasItem( sameInstant( DEFAULT_CREATED ) ) ) )
                        .andExpect( jsonPath( "$.[*].updated" ).value( hasItem( sameInstant( DEFAULT_UPDATED ) ) ) )
                        .andExpect( jsonPath( "$.[*].state" ).value( hasItem( DEFAULT_STATE.toString() ) ) )
                        .andExpect(
                                jsonPath( "$.[*].deliveryType" ).value( hasItem( DEFAULT_DELIVERY_TYPE.toString() ) ) )
                        .andExpect( jsonPath( "$.[*].includeBatteries" )
                                            .value( hasItem( DEFAULT_INCLUDE_BATTERIES.booleanValue() ) ) )
                        .andExpect( jsonPath( "$.[*].description" ).value( hasItem( DEFAULT_DESCRIPTION.toString() ) ) )
                        .andExpect( jsonPath( "$.[*].deliveryPrice" )
                                            .value( hasItem( DEFAULT_DELIVERY_PRICE.doubleValue() ) ) );
    }

    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush( order );

        // Get the order
        restOrderMockMvc.perform( get( "/api/orders/{id}", order.getId() ) )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                        .andExpect( jsonPath( "$.id" ).value( order.getId().intValue() ) )
                        .andExpect( jsonPath( "$.created" ).value( sameInstant( DEFAULT_CREATED ) ) )
                        .andExpect( jsonPath( "$.updated" ).value( sameInstant( DEFAULT_UPDATED ) ) )
                        .andExpect( jsonPath( "$.state" ).value( DEFAULT_STATE.toString() ) )
                        .andExpect( jsonPath( "$.deliveryType" ).value( DEFAULT_DELIVERY_TYPE.toString() ) )
                        .andExpect( jsonPath( "$.includeBatteries" ).value( DEFAULT_INCLUDE_BATTERIES.booleanValue() ) )
                        .andExpect( jsonPath( "$.description" ).value( DEFAULT_DESCRIPTION.toString() ) )
                        .andExpect( jsonPath( "$.deliveryPrice" ).value( DEFAULT_DELIVERY_PRICE.doubleValue() ) );
    }

    @Test
    @Transactional
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform( get( "/api/orders/{id}", Long.MAX_VALUE ) )
                        .andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void updateOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush( order );
        orderSearchRepository.save( order );
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findOne( order.getId() );
        updatedOrder
                .created( UPDATED_CREATED )
                .updated( UPDATED_UPDATED )
                .state( UPDATED_STATE )
                .deliveryType( UPDATED_DELIVERY_TYPE )
                .includeBatteries( UPDATED_INCLUDE_BATTERIES )
                .description( UPDATED_DESCRIPTION );
        OrderDTO orderDTO = orderMapper.toDto( updatedOrder );

        restOrderMockMvc.perform( put( "/api/orders" )
                                          .contentType( TestUtil.APPLICATION_JSON_UTF8 )
                                          .content( TestUtil.convertObjectToJsonBytes( orderDTO ) ) )
                        .andExpect( status().isOk() );

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat( orderList ).hasSize( databaseSizeBeforeUpdate );
        Order testOrder = orderList.get( orderList.size() - 1 );
        assertThat( testOrder.getCreated() ).isEqualTo( UPDATED_CREATED );
        assertThat( testOrder.getState() ).isEqualTo( UPDATED_STATE );
        assertThat( testOrder.getDeliveryType() ).isEqualTo( UPDATED_DELIVERY_TYPE );
        assertThat( testOrder.isIncludeBatteries() ).isEqualTo( UPDATED_INCLUDE_BATTERIES );
        assertThat( testOrder.getDescription() ).isEqualTo( UPDATED_DESCRIPTION );

        // Validate the Order in Elasticsearch
        Order orderEs = orderSearchRepository.findOne( testOrder.getId() );
        assertThat( orderEs ).isEqualToIgnoringGivenFields( testOrder, "updated", "created" );
    }

    @Test
    @Transactional
    public void updateNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto( order );

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrderMockMvc.perform( put( "/api/orders" )
                                          .contentType( TestUtil.APPLICATION_JSON_UTF8 )
                                          .content( TestUtil.convertObjectToJsonBytes( orderDTO ) ) )
                        .andExpect( status().isCreated() );

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat( orderList ).hasSize( databaseSizeBeforeUpdate + 1 );
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush( order );
        orderSearchRepository.save( order );
        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Get the order
        restOrderMockMvc.perform( delete( "/api/orders/{id}", order.getId() )
                                          .accept( TestUtil.APPLICATION_JSON_UTF8 ) )
                        .andExpect( status().isOk() );

        // Validate Elasticsearch is empty
        boolean orderExistsInEs = orderSearchRepository.exists( order.getId() );
        assertThat( orderExistsInEs ).isFalse();

        // Validate the database is empty
        List<Order> orderList = orderRepository.findAll();
        assertThat( orderList ).hasSize( databaseSizeBeforeDelete - 1 );
    }

    @Test
    @Transactional
    public void searchOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush( order );
        orderSearchRepository.save( order );

        // Search the order
        restOrderMockMvc.perform( get( "/api/_search/orders?query=id:" + order.getId() ) )
                        .andExpect( status().isOk() )
                        .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                        .andExpect( jsonPath( "$.[*].id" ).value( hasItem( order.getId().intValue() ) ) )
                        .andExpect( jsonPath( "$.[*].created" ).value( hasItem( sameInstant( DEFAULT_CREATED ) ) ) )
                        .andExpect( jsonPath( "$.[*].updated" ).value( hasItem( sameInstant( DEFAULT_UPDATED ) ) ) )
                        .andExpect( jsonPath( "$.[*].state" ).value( hasItem( DEFAULT_STATE.toString() ) ) )
                        .andExpect(
                                jsonPath( "$.[*].deliveryType" ).value( hasItem( DEFAULT_DELIVERY_TYPE.toString() ) ) )
                        .andExpect( jsonPath( "$.[*].includeBatteries" )
                                            .value( hasItem( DEFAULT_INCLUDE_BATTERIES.booleanValue() ) ) )
                        .andExpect( jsonPath( "$.[*].description" ).value( hasItem( DEFAULT_DESCRIPTION.toString() ) ) )
                        .andExpect( jsonPath( "$.[*].deliveryPrice" )
                                            .value( hasItem( DEFAULT_DELIVERY_PRICE.doubleValue() ) ) );
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier( Order.class );
        Order order1 = new Order();
        order1.setId( 1L );
        Order order2 = new Order();
        order2.setId( order1.getId() );
        assertThat( order1 ).isEqualTo( order2 );
        order2.setId( 2L );
        assertThat( order1 ).isNotEqualTo( order2 );
        order1.setId( null );
        assertThat( order1 ).isNotEqualTo( order2 );
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier( OrderDTO.class );
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setId( 1L );
        OrderDTO orderDTO2 = new OrderDTO();
        assertThat( orderDTO1 ).isNotEqualTo( orderDTO2 );
        orderDTO2.setId( orderDTO1.getId() );
        assertThat( orderDTO1 ).isEqualTo( orderDTO2 );
        orderDTO2.setId( 2L );
        assertThat( orderDTO1 ).isNotEqualTo( orderDTO2 );
        orderDTO1.setId( null );
        assertThat( orderDTO1 ).isNotEqualTo( orderDTO2 );
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat( orderMapper.fromId( 42L ).getId() ).isEqualTo( 42 );
        assertThat( orderMapper.fromId( null ) ).isNull();
    }
}
