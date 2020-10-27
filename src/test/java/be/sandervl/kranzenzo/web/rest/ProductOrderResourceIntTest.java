package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.KranzenzoApp;
import be.sandervl.kranzenzo.config.DummyS3Configuration;
import be.sandervl.kranzenzo.domain.Product;
import be.sandervl.kranzenzo.domain.ProductOrder;
import be.sandervl.kranzenzo.domain.enumeration.DeliveryType;
import be.sandervl.kranzenzo.domain.enumeration.OrderState;
import be.sandervl.kranzenzo.domain.enumeration.PaymentType;
import be.sandervl.kranzenzo.repository.ProductOrderRepository;
import be.sandervl.kranzenzo.service.ProductOrderService;
import be.sandervl.kranzenzo.service.ProductService;
import be.sandervl.kranzenzo.service.dto.ProductOrderDTO;
import be.sandervl.kranzenzo.service.mapper.ProductOrderMapper;
import be.sandervl.kranzenzo.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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

import static be.sandervl.kranzenzo.web.rest.TestUtil.createFormattingConversionService;
import static be.sandervl.kranzenzo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductOrderResource REST controller.
 *
 * @see ProductOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KranzenzoApp.class)
@Import(DummyS3Configuration.class)
public class ProductOrderResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant( Instant
        .ofEpochMilli( 0L ), ZoneOffset.UTC );
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now( ZoneId.systemDefault() ).withNano( 0 );

    private static final ZonedDateTime DEFAULT_UPDATED = ZonedDateTime.ofInstant( Instant
        .ofEpochMilli( 0L ), ZoneOffset.UTC );
    private static final ZonedDateTime UPDATED_UPDATED = ZonedDateTime.now( ZoneId.systemDefault() ).withNano( 0 );

    private static final OrderState DEFAULT_STATE = OrderState.NEW;
    private static final OrderState UPDATED_STATE = OrderState.CANCELLED;

    private static final DeliveryType DEFAULT_DELIVERY_TYPE = DeliveryType.DELIVERED;
    private static final DeliveryType UPDATED_DELIVERY_TYPE = DeliveryType.PICKUP;

    private static final Boolean DEFAULT_INCLUDE_BATTERIES = false;
    private static final Boolean UPDATED_INCLUDE_BATTERIES = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_DELIVERY_PRICE = 0F;
    private static final Float UPDATED_DELIVERY_PRICE = 1F;

    private static final PaymentType DEFAULT_PAYMENT_TYPE = PaymentType.CASH;
    private static final PaymentType UPDATED_PAYMENT_TYPE = PaymentType.TRANSFER;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductOrderMockMvc;

    private ProductOrder productOrder;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createEntity( EntityManager em ) {
        ProductOrder productOrder = new ProductOrder()
            .created( DEFAULT_CREATED )
            .updated( DEFAULT_UPDATED )
            .state( DEFAULT_STATE )
            .deliveryType( DEFAULT_DELIVERY_TYPE )
            .includeBatteries( DEFAULT_INCLUDE_BATTERIES )
            .description( DEFAULT_DESCRIPTION )
            .deliveryPrice( DEFAULT_DELIVERY_PRICE )
            .paymentType( DEFAULT_PAYMENT_TYPE );
        // Add required entity
        Product product = ProductResourceIntTest.createEntity( em );
        em.persist( product );
        em.flush();
        productOrder.setProduct( product );
        return productOrder;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks( this );
        final ProductOrderResource productOrderResource = new ProductOrderResource( productOrderService, productService );
        this.restProductOrderMockMvc = MockMvcBuilders.standaloneSetup( productOrderResource )
                                                      .setCustomArgumentResolvers( pageableArgumentResolver )
                                                      .setControllerAdvice( exceptionTranslator )
                                                      .setConversionService( createFormattingConversionService() )
                                                      .setMessageConverters( jacksonMessageConverter ).build();
    }

    @Before
    public void initTest() {
        productOrder = createEntity( em );
    }

    @Test
    @Transactional
    public void createProductOrder() throws Exception {
        int databaseSizeBeforeCreate = productOrderRepository.findAll().size();

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto( productOrder );
        restProductOrderMockMvc.perform( post( "/api/product-orders" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productOrderDTO ) ) )
                               .andExpect( status().isCreated() );

        // Validate the ProductOrder in the database
        List <ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat( productOrderList ).hasSize( databaseSizeBeforeCreate + 1 );
        ProductOrder testProductOrder = productOrderList.get( productOrderList.size() - 1 );
        assertThat( testProductOrder.getState() ).isEqualTo( DEFAULT_STATE );
        assertThat( testProductOrder.getDeliveryType() ).isEqualTo( DEFAULT_DELIVERY_TYPE );
        assertThat( testProductOrder.isIncludeBatteries() ).isEqualTo( DEFAULT_INCLUDE_BATTERIES );
        assertThat( testProductOrder.getDescription() ).isEqualTo( DEFAULT_DESCRIPTION );
        assertThat( testProductOrder.getDeliveryPrice() ).isEqualTo( DEFAULT_DELIVERY_PRICE );
        assertThat( testProductOrder.getPaymentType() ).isEqualTo( DEFAULT_PAYMENT_TYPE );
    }

    @Test
    @Transactional
    public void createProductOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productOrderRepository.findAll().size();

        // Create the ProductOrder with an existing ID
        productOrder.setId( 1L );
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto( productOrder );

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOrderMockMvc.perform( post( "/api/product-orders" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productOrderDTO ) ) )
                               .andExpect( status().isBadRequest() );

        // Validate the ProductOrder in the database
        List <ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat( productOrderList ).hasSize( databaseSizeBeforeCreate );
    }

    @Test
    @Transactional
    public void checkPaymentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setPaymentType( null );

        // Create the ProductOrder, which fails.
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto( productOrder );

        restProductOrderMockMvc.perform( post( "/api/product-orders" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productOrderDTO ) ) )
                               .andExpect( status().isBadRequest() );

        List <ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat( productOrderList ).hasSize( databaseSizeBeforeTest );
    }

    @Test
    @Transactional
    public void getAllProductOrders() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush( productOrder );

        // Get all the productOrderList
        restProductOrderMockMvc.perform( get( "/api/product-orders?sort=id,desc" ) )
                               .andExpect( status().isOk() )
                               .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                               .andExpect( jsonPath( "$.[*].id" ).value( hasItem( productOrder.getId().intValue() ) ) )
                               .andExpect( jsonPath( "$.[*].created" )
                                   .value( hasItem( sameInstant( DEFAULT_CREATED ) ) ) )
                               .andExpect( jsonPath( "$.[*].updated" )
                                   .value( hasItem( sameInstant( DEFAULT_UPDATED ) ) ) )
                               .andExpect( jsonPath( "$.[*].state" ).value( hasItem( DEFAULT_STATE.toString() ) ) )
                               .andExpect( jsonPath( "$.[*].deliveryType" )
                                   .value( hasItem( DEFAULT_DELIVERY_TYPE.toString() ) ) )
                               .andExpect( jsonPath( "$.[*].includeBatteries" )
                                   .value( hasItem( DEFAULT_INCLUDE_BATTERIES.booleanValue() ) ) )
                               .andExpect( jsonPath( "$.[*].description" )
                                   .value( hasItem( DEFAULT_DESCRIPTION.toString() ) ) )
                               .andExpect( jsonPath( "$.[*].deliveryPrice" )
                                   .value( hasItem( DEFAULT_DELIVERY_PRICE.doubleValue() ) ) )
                               .andExpect( jsonPath( "$.[*].paymentType" )
                                   .value( hasItem( DEFAULT_PAYMENT_TYPE.toString() ) ) );
    }

    @Test
    @Transactional
    public void getProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush( productOrder );

        // Get the productOrder
        restProductOrderMockMvc.perform( get( "/api/product-orders/{id}", productOrder.getId() ) )
                               .andExpect( status().isOk() )
                               .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                               .andExpect( jsonPath( "$.id" ).value( productOrder.getId().intValue() ) )
                               .andExpect( jsonPath( "$.created" ).value( sameInstant( DEFAULT_CREATED ) ) )
                               .andExpect( jsonPath( "$.updated" ).value( sameInstant( DEFAULT_UPDATED ) ) )
                               .andExpect( jsonPath( "$.state" ).value( DEFAULT_STATE.toString() ) )
                               .andExpect( jsonPath( "$.deliveryType" ).value( DEFAULT_DELIVERY_TYPE.toString() ) )
                               .andExpect( jsonPath( "$.includeBatteries" )
                                   .value( DEFAULT_INCLUDE_BATTERIES.booleanValue() ) )
                               .andExpect( jsonPath( "$.description" ).value( DEFAULT_DESCRIPTION.toString() ) )
                               .andExpect( jsonPath( "$.deliveryPrice" ).value( DEFAULT_DELIVERY_PRICE.doubleValue() ) )
                               .andExpect( jsonPath( "$.paymentType" ).value( DEFAULT_PAYMENT_TYPE.toString() ) );
    }

    @Test
    @Transactional
    public void getNonExistingProductOrder() throws Exception {
        // Get the productOrder
        restProductOrderMockMvc.perform( get( "/api/product-orders/{id}", Long.MAX_VALUE ) )
                               .andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void updateProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush( productOrder );

        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();

        // Update the productOrder
        ProductOrder updatedProductOrder = productOrderRepository.findById( productOrder.getId() ).get();
        // Disconnect from session so that the updates on updatedProductOrder are not directly saved in db
        em.detach( updatedProductOrder );
        updatedProductOrder
            .created( UPDATED_CREATED )
            .updated( UPDATED_UPDATED )
            .state( UPDATED_STATE )
            .deliveryType( UPDATED_DELIVERY_TYPE )
            .includeBatteries( UPDATED_INCLUDE_BATTERIES )
            .description( UPDATED_DESCRIPTION )
            .deliveryPrice( UPDATED_DELIVERY_PRICE )
            .state( OrderState.CANCELLED )
            .paymentType( UPDATED_PAYMENT_TYPE );
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto( updatedProductOrder );

        restProductOrderMockMvc.perform( put( "/api/product-orders" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productOrderDTO ) ) )
                               .andExpect( status().isOk() );

        // Validate the ProductOrder in the database
        List <ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat( productOrderList ).hasSize( databaseSizeBeforeUpdate );
        ProductOrder testProductOrder = productOrderList.get( productOrderList.size() - 1 );
        assertThat( testProductOrder.getCreated() ).isEqualTo( UPDATED_CREATED );
        assertThat( testProductOrder.getUpdated() ).isEqualTo( UPDATED_UPDATED );
        assertThat( testProductOrder.getState() ).isEqualTo( UPDATED_STATE );
        assertThat( testProductOrder.getDeliveryType() ).isEqualTo( UPDATED_DELIVERY_TYPE );
        assertThat( testProductOrder.isIncludeBatteries() ).isEqualTo( UPDATED_INCLUDE_BATTERIES );
        assertThat( testProductOrder.getDescription() ).isEqualTo( UPDATED_DESCRIPTION );
        assertThat( testProductOrder.getDeliveryPrice() ).isEqualTo( UPDATED_DELIVERY_PRICE );
        assertThat( testProductOrder.getPaymentType() ).isEqualTo( UPDATED_PAYMENT_TYPE );
    }

    @Test
    @Transactional
    public void updateNonExistingProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto( productOrder );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderMockMvc.perform( put( "/api/product-orders" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productOrderDTO ) ) )
                               .andExpect( status().isBadRequest() );

        // Validate the ProductOrder in the database
        List <ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat( productOrderList ).hasSize( databaseSizeBeforeUpdate );
    }

    @Test
    @Transactional
    public void deleteProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush( productOrder );

        int databaseSizeBeforeDelete = productOrderRepository.findAll().size();

        // Get the productOrder
        restProductOrderMockMvc.perform( delete( "/api/product-orders/{id}", productOrder.getId() )
            .accept( TestUtil.APPLICATION_JSON_UTF8 ) )
                               .andExpect( status().isOk() );

        // Validate the database is empty
        List <ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat( productOrderList ).hasSize( databaseSizeBeforeDelete - 1 );
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier( ProductOrder.class );
        ProductOrder productOrder1 = new ProductOrder();
        productOrder1.setId( 1L );
        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setId( productOrder1.getId() );
        assertThat( productOrder1 ).isEqualTo( productOrder2 );
        productOrder2.setId( 2L );
        assertThat( productOrder1 ).isNotEqualTo( productOrder2 );
        productOrder1.setId( null );
        assertThat( productOrder1 ).isNotEqualTo( productOrder2 );
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier( ProductOrderDTO.class );
        ProductOrderDTO productOrderDTO1 = new ProductOrderDTO();
        productOrderDTO1.setId( 1L );
        ProductOrderDTO productOrderDTO2 = new ProductOrderDTO();
        assertThat( productOrderDTO1 ).isNotEqualTo( productOrderDTO2 );
        productOrderDTO2.setId( productOrderDTO1.getId() );
        assertThat( productOrderDTO1 ).isEqualTo( productOrderDTO2 );
        productOrderDTO2.setId( 2L );
        assertThat( productOrderDTO1 ).isNotEqualTo( productOrderDTO2 );
        productOrderDTO1.setId( null );
        assertThat( productOrderDTO1 ).isNotEqualTo( productOrderDTO2 );
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat( productOrderMapper.fromId( 42L ).getId() ).isEqualTo( 42 );
        assertThat( productOrderMapper.fromId( null ) ).isNull();
    }
}
