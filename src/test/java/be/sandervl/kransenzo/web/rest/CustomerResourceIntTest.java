package be.sandervl.kransenzo.web.rest;

import be.sandervl.kransenzo.KransenzoApp;
import be.sandervl.kransenzo.domain.Customer;
import be.sandervl.kransenzo.repository.CustomerRepository;
import be.sandervl.kransenzo.repository.search.CustomerSearchRepository;
import be.sandervl.kransenzo.service.dto.CustomerDTO;
import be.sandervl.kransenzo.service.mapper.CustomerMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KransenzoApp.class)
public class CustomerResourceIntTest
{

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_ZIP_CODE = 1000;
    private static final Integer UPDATED_ZIP_CODE = 1001;

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerSearchRepository customerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity( EntityManager em ) {
        Customer customer = new Customer()
                .street( DEFAULT_STREET )
                .city( DEFAULT_CITY )
                .zipCode( DEFAULT_ZIP_CODE )
                .province( DEFAULT_PROVINCE )
                .phoneNumber( DEFAULT_PHONE_NUMBER );
        return customer;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks( this );
        CustomerResource customerResource = new CustomerResource( customerRepository, customerMapper,
                                                                  customerSearchRepository );
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup( customerResource )
                                                  .setCustomArgumentResolvers( pageableArgumentResolver )
                                                  .setControllerAdvice( exceptionTranslator )
                                                  .setMessageConverters( jacksonMessageConverter ).build();
    }

    @Before
    public void initTest() {
        customerSearchRepository.deleteAll();
        customer = createEntity( em );
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto( customer );
        restCustomerMockMvc.perform( post( "/api/customers" )
                                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
                                             .content( TestUtil.convertObjectToJsonBytes( customerDTO ) ) )
                           .andExpect( status().isCreated() );

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat( customerList ).hasSize( databaseSizeBeforeCreate + 1 );
        Customer testCustomer = customerList.get( customerList.size() - 1 );
        assertThat( testCustomer.getStreet() ).isEqualTo( DEFAULT_STREET );
        assertThat( testCustomer.getCity() ).isEqualTo( DEFAULT_CITY );
        assertThat( testCustomer.getZipCode() ).isEqualTo( DEFAULT_ZIP_CODE );
        assertThat( testCustomer.getProvince() ).isEqualTo( DEFAULT_PROVINCE );
        assertThat( testCustomer.getPhoneNumber() ).isEqualTo( DEFAULT_PHONE_NUMBER );

        // Validate the Customer in Elasticsearch
        Customer customerEs = customerSearchRepository.findOne( testCustomer.getId() );
        assertThat( customerEs ).isEqualToComparingFieldByField( testCustomer );
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId( 1L );
        CustomerDTO customerDTO = customerMapper.toDto( customer );

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform( post( "/api/customers" )
                                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
                                             .content( TestUtil.convertObjectToJsonBytes( customerDTO ) ) )
                           .andExpect( status().isBadRequest() );

        // Validate the Alice in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat( customerList ).hasSize( databaseSizeBeforeCreate );
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush( customer );

        // Get all the customerList
        restCustomerMockMvc.perform( get( "/api/customers?sort=id,desc" ) )
                           .andExpect( status().isOk() )
                           .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                           .andExpect( jsonPath( "$.[*].id" ).value( hasItem( customer.getId().intValue() ) ) )
                           .andExpect( jsonPath( "$.[*].street" ).value( hasItem( DEFAULT_STREET.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].city" ).value( hasItem( DEFAULT_CITY.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].zipCode" ).value( hasItem( DEFAULT_ZIP_CODE ) ) )
                           .andExpect( jsonPath( "$.[*].province" ).value( hasItem( DEFAULT_PROVINCE.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].phoneNumber" )
                                               .value( hasItem( DEFAULT_PHONE_NUMBER.toString() ) ) );
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush( customer );

        // Get the customer
        restCustomerMockMvc.perform( get( "/api/customers/{id}", customer.getId() ) )
                           .andExpect( status().isOk() )
                           .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                           .andExpect( jsonPath( "$.id" ).value( customer.getId().intValue() ) )
                           .andExpect( jsonPath( "$.street" ).value( DEFAULT_STREET.toString() ) )
                           .andExpect( jsonPath( "$.city" ).value( DEFAULT_CITY.toString() ) )
                           .andExpect( jsonPath( "$.zipCode" ).value( DEFAULT_ZIP_CODE ) )
                           .andExpect( jsonPath( "$.province" ).value( DEFAULT_PROVINCE.toString() ) )
                           .andExpect( jsonPath( "$.phoneNumber" ).value( DEFAULT_PHONE_NUMBER.toString() ) );
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform( get( "/api/customers/{id}", Long.MAX_VALUE ) )
                           .andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush( customer );
        customerSearchRepository.save( customer );
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findOne( customer.getId() );
        updatedCustomer
                .street( UPDATED_STREET )
                .city( UPDATED_CITY )
                .zipCode( UPDATED_ZIP_CODE )
                .province( UPDATED_PROVINCE )
                .phoneNumber( UPDATED_PHONE_NUMBER );
        CustomerDTO customerDTO = customerMapper.toDto( updatedCustomer );

        restCustomerMockMvc.perform( put( "/api/customers" )
                                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
                                             .content( TestUtil.convertObjectToJsonBytes( customerDTO ) ) )
                           .andExpect( status().isOk() );

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat( customerList ).hasSize( databaseSizeBeforeUpdate );
        Customer testCustomer = customerList.get( customerList.size() - 1 );
        assertThat( testCustomer.getStreet() ).isEqualTo( UPDATED_STREET );
        assertThat( testCustomer.getCity() ).isEqualTo( UPDATED_CITY );
        assertThat( testCustomer.getZipCode() ).isEqualTo( UPDATED_ZIP_CODE );
        assertThat( testCustomer.getProvince() ).isEqualTo( UPDATED_PROVINCE );
        assertThat( testCustomer.getPhoneNumber() ).isEqualTo( UPDATED_PHONE_NUMBER );

        // Validate the Customer in Elasticsearch
        Customer customerEs = customerSearchRepository.findOne( testCustomer.getId() );
        assertThat( customerEs ).isEqualToComparingFieldByField( testCustomer );
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto( customer );

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomerMockMvc.perform( put( "/api/customers" )
                                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
                                             .content( TestUtil.convertObjectToJsonBytes( customerDTO ) ) )
                           .andExpect( status().isCreated() );

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat( customerList ).hasSize( databaseSizeBeforeUpdate + 1 );
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush( customer );
        customerSearchRepository.save( customer );
        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform( delete( "/api/customers/{id}", customer.getId() )
                                             .accept( TestUtil.APPLICATION_JSON_UTF8 ) )
                           .andExpect( status().isOk() );

        // Validate Elasticsearch is empty
        boolean customerExistsInEs = customerSearchRepository.exists( customer.getId() );
        assertThat( customerExistsInEs ).isFalse();

        // Validate the database is empty
        List<Customer> customerList = customerRepository.findAll();
        assertThat( customerList ).hasSize( databaseSizeBeforeDelete - 1 );
    }

    @Test
    @Transactional
    public void searchCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush( customer );
        customerSearchRepository.save( customer );

        // Search the customer
        restCustomerMockMvc.perform( get( "/api/_search/customers?query=id:" + customer.getId() ) )
                           .andExpect( status().isOk() )
                           .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                           .andExpect( jsonPath( "$.[*].id" ).value( hasItem( customer.getId().intValue() ) ) )
                           .andExpect( jsonPath( "$.[*].street" ).value( hasItem( DEFAULT_STREET.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].city" ).value( hasItem( DEFAULT_CITY.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].zipCode" ).value( hasItem( DEFAULT_ZIP_CODE ) ) )
                           .andExpect( jsonPath( "$.[*].province" ).value( hasItem( DEFAULT_PROVINCE.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].phoneNumber" )
                                               .value( hasItem( DEFAULT_PHONE_NUMBER.toString() ) ) );
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier( Customer.class );
        Customer customer1 = new Customer();
        customer1.setId( 1L );
        Customer customer2 = new Customer();
        customer2.setId( customer1.getId() );
        assertThat( customer1 ).isEqualTo( customer2 );
        customer2.setId( 2L );
        assertThat( customer1 ).isNotEqualTo( customer2 );
        customer1.setId( null );
        assertThat( customer1 ).isNotEqualTo( customer2 );
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier( CustomerDTO.class );
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setId( 1L );
        CustomerDTO customerDTO2 = new CustomerDTO();
        assertThat( customerDTO1 ).isNotEqualTo( customerDTO2 );
        customerDTO2.setId( customerDTO1.getId() );
        assertThat( customerDTO1 ).isEqualTo( customerDTO2 );
        customerDTO2.setId( 2L );
        assertThat( customerDTO1 ).isNotEqualTo( customerDTO2 );
        customerDTO1.setId( null );
        assertThat( customerDTO1 ).isNotEqualTo( customerDTO2 );
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat( customerMapper.fromId( 42L ).getId() ).isEqualTo( 42 );
        assertThat( customerMapper.fromId( null ) ).isNull();
    }
}
