package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.KranzenzoApp;
import be.sandervl.kranzenzo.config.DummyS3Configuration;
import be.sandervl.kranzenzo.domain.Product;
import be.sandervl.kranzenzo.repository.ProductRepository;
import be.sandervl.kranzenzo.service.ProductService;
import be.sandervl.kranzenzo.service.dto.ProductDTO;
import be.sandervl.kranzenzo.service.mapper.ProductMapper;
import be.sandervl.kranzenzo.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static be.sandervl.kranzenzo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KranzenzoApp.class)
@Import(DummyS3Configuration.class)
public class ProductResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 0F;
    private static final Float UPDATED_PRICE = 1F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = true;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_NUMBER_OF_BATTERIES = 0;
    private static final Integer UPDATED_NUMBER_OF_BATTERIES = 1;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Autowired
    private ProductMapper productMapper;

    @Mock
    private ProductService productServiceMock;

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

    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity( EntityManager em ) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .nameAsInteger(0)
            .price( DEFAULT_PRICE )
            .description( DEFAULT_DESCRIPTION )
            .isActive( DEFAULT_IS_ACTIVE )
            .numberOfBatteries( DEFAULT_NUMBER_OF_BATTERIES );
        return product;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks( this );
        final ProductResource productResource = new ProductResource( productService );
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup( productResource )
                                                 .setCustomArgumentResolvers( pageableArgumentResolver )
                                                 .setControllerAdvice( exceptionTranslator )
                                                 .setConversionService( createFormattingConversionService() )
                                                 .setMessageConverters( jacksonMessageConverter ).build();
    }

    @Before
    public void initTest() {
        product = createEntity( em );
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto( product );
        restProductMockMvc.perform( post( "/api/products" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productDTO ) ) )
                          .andExpect( status().isCreated() );

        // Validate the Product in the database
        List <Product> productList = productRepository.findAll();
        assertThat( productList ).hasSize( databaseSizeBeforeCreate + 1 );
        Product testProduct = productList.get( productList.size() - 1 );
        assertThat( testProduct.getName() ).isEqualTo( DEFAULT_NAME );
        assertThat( testProduct.getPrice() ).isEqualTo( DEFAULT_PRICE );
        assertThat( testProduct.getDescription() ).isEqualTo( DEFAULT_DESCRIPTION );
        assertThat( testProduct.isIsActive() ).isEqualTo( DEFAULT_IS_ACTIVE );
        assertThat( testProduct.getNumberOfBatteries() ).isEqualTo( DEFAULT_NUMBER_OF_BATTERIES );
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId( 1L );
        ProductDTO productDTO = productMapper.toDto( product );

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform( post( "/api/products" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productDTO ) ) )
                          .andExpect( status().isBadRequest() );

        // Validate the Product in the database
        List <Product> productList = productRepository.findAll();
        assertThat( productList ).hasSize( databaseSizeBeforeCreate );
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName( null );

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto( product );

        restProductMockMvc.perform( post( "/api/products" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productDTO ) ) )
                          .andExpect( status().isBadRequest() );

        List <Product> productList = productRepository.findAll();
        assertThat( productList ).hasSize( databaseSizeBeforeTest );
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setPrice( null );

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto( product );

        restProductMockMvc.perform( post( "/api/products" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productDTO ) ) )
                          .andExpect( status().isBadRequest() );

        List <Product> productList = productRepository.findAll();
        assertThat( productList ).hasSize( databaseSizeBeforeTest );
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush( product );

        // Get all the productList
        restProductMockMvc.perform( get( "/api/products?sort=id,desc" ) )
                          .andExpect( status().isOk() )
                          .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                          .andExpect( jsonPath( "$.[*].id" ).value( hasItem( product.getId().intValue() ) ) )
                          .andExpect( jsonPath( "$.[*].name" ).value( hasItem( DEFAULT_NAME.toString() ) ) )
                          .andExpect( jsonPath( "$.[*].price" ).value( hasItem( DEFAULT_PRICE.doubleValue() ) ) )
                          .andExpect( jsonPath( "$.[*].description" )
                              .value( hasItem( DEFAULT_DESCRIPTION.toString() ) ) )
                          .andExpect( jsonPath( "$.[*].isActive" )
                              .value( hasItem( DEFAULT_IS_ACTIVE.booleanValue() ) ) )
                          .andExpect( jsonPath( "$.[*].numberOfBatteries" )
                              .value( hasItem( DEFAULT_NUMBER_OF_BATTERIES ) ) );
    }

    public void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        ProductResource productResource = new ProductResource( productServiceMock );
        when( productServiceMock.findAllWithEagerRelationships( any() ) )
            .thenReturn( new PageImpl( new ArrayList <>() ) );

        MockMvc restProductMockMvc = MockMvcBuilders.standaloneSetup( productResource )
                                                    .setCustomArgumentResolvers( pageableArgumentResolver )
                                                    .setControllerAdvice( exceptionTranslator )
                                                    .setConversionService( createFormattingConversionService() )
                                                    .setMessageConverters( jacksonMessageConverter ).build();

        restProductMockMvc.perform( get( "/api/products?eagerload=true" ) )
                          .andExpect( status().isOk() );

        verify( productServiceMock, times( 1 ) ).findAllWithEagerRelationships( any() );
    }

    public void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ProductResource productResource = new ProductResource( productServiceMock );
        when( productServiceMock.findAllWithEagerRelationships( any() ) )
            .thenReturn( new PageImpl( new ArrayList <>() ) );
        MockMvc restProductMockMvc = MockMvcBuilders.standaloneSetup( productResource )
                                                    .setCustomArgumentResolvers( pageableArgumentResolver )
                                                    .setControllerAdvice( exceptionTranslator )
                                                    .setConversionService( createFormattingConversionService() )
                                                    .setMessageConverters( jacksonMessageConverter ).build();

        restProductMockMvc.perform( get( "/api/products?eagerload=true" ) )
                          .andExpect( status().isOk() );

        verify( productServiceMock, times( 1 ) ).findAllWithEagerRelationships( any() );
    }

    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush( product );

        // Get the product
        restProductMockMvc.perform( get( "/api/products/{id}", product.getId() ) )
                          .andExpect( status().isOk() )
                          .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                          .andExpect( jsonPath( "$.id" ).value( product.getId().intValue() ) )
                          .andExpect( jsonPath( "$.name" ).value( DEFAULT_NAME.toString() ) )
                          .andExpect( jsonPath( "$.price" ).value( DEFAULT_PRICE.doubleValue() ) )
                          .andExpect( jsonPath( "$.description" ).value( DEFAULT_DESCRIPTION.toString() ) )
                          .andExpect( jsonPath( "$.isActive" ).value( DEFAULT_IS_ACTIVE.booleanValue() ) )
                          .andExpect( jsonPath( "$.numberOfBatteries" ).value( DEFAULT_NUMBER_OF_BATTERIES ) );
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform( get( "/api/products/{id}", Long.MAX_VALUE ) )
                          .andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush( product );

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById( product.getId() ).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach( updatedProduct );
        updatedProduct
            .name( UPDATED_NAME )
            .price( UPDATED_PRICE )
            .description( UPDATED_DESCRIPTION )
            .isActive( UPDATED_IS_ACTIVE )
            .numberOfBatteries( UPDATED_NUMBER_OF_BATTERIES );
        ProductDTO productDTO = productMapper.toDto( updatedProduct );

        restProductMockMvc.perform( put( "/api/products" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productDTO ) ) )
                          .andExpect( status().isOk() );

        // Validate the Product in the database
        List <Product> productList = productRepository.findAll();
        assertThat( productList ).hasSize( databaseSizeBeforeUpdate );
        Product testProduct = productList.get( productList.size() - 1 );
        assertThat( testProduct.getName() ).isEqualTo( UPDATED_NAME );
        assertThat( testProduct.getPrice() ).isEqualTo( UPDATED_PRICE );
        assertThat( testProduct.getDescription() ).isEqualTo( UPDATED_DESCRIPTION );
        assertThat( testProduct.isIsActive() ).isEqualTo( UPDATED_IS_ACTIVE );
        assertThat( testProduct.getNumberOfBatteries() ).isEqualTo( UPDATED_NUMBER_OF_BATTERIES );
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto( product );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform( put( "/api/products" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( productDTO ) ) )
                          .andExpect( status().isBadRequest() );

        // Validate the Product in the database
        List <Product> productList = productRepository.findAll();
        assertThat( productList ).hasSize( databaseSizeBeforeUpdate );
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush( product );

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Get the product
        restProductMockMvc.perform( delete( "/api/products/{id}", product.getId() )
            .accept( TestUtil.APPLICATION_JSON_UTF8 ) )
                          .andExpect( status().isOk() );

        // Validate the database is empty
        List <Product> productList = productRepository.findAll();
        assertThat( productList ).hasSize( databaseSizeBeforeDelete - 1 );
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier( Product.class );
        Product product1 = new Product();
        product1.setId( 1L );
        Product product2 = new Product();
        product2.setId( product1.getId() );
        assertThat( product1 ).isEqualTo( product2 );
        product2.setId( 2L );
        assertThat( product1 ).isNotEqualTo( product2 );
        product1.setId( null );
        assertThat( product1 ).isNotEqualTo( product2 );
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier( ProductDTO.class );
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId( 1L );
        ProductDTO productDTO2 = new ProductDTO();
        assertThat( productDTO1 ).isNotEqualTo( productDTO2 );
        productDTO2.setId( productDTO1.getId() );
        assertThat( productDTO1 ).isEqualTo( productDTO2 );
        productDTO2.setId( 2L );
        assertThat( productDTO1 ).isNotEqualTo( productDTO2 );
        productDTO1.setId( null );
        assertThat( productDTO1 ).isNotEqualTo( productDTO2 );
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat( productMapper.fromId( 42L ).getId() ).isEqualTo( 42 );
        assertThat( productMapper.fromId( null ) ).isNull();
    }
}
