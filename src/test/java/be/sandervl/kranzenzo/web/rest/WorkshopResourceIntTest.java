package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.KranzenzoApp;
import be.sandervl.kranzenzo.config.DummyS3Configuration;
import be.sandervl.kranzenzo.domain.Workshop;
import be.sandervl.kranzenzo.repository.ImageRepository;
import be.sandervl.kranzenzo.repository.WorkshopDateRepository;
import be.sandervl.kranzenzo.repository.WorkshopRepository;
import be.sandervl.kranzenzo.repository.WorkshopSubscriptionRepository;
import be.sandervl.kranzenzo.service.dto.WorkshopDTO;
import be.sandervl.kranzenzo.service.mapper.WorkshopMapper;
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
 * Test class for the WorkshopResource REST controller.
 *
 * @see WorkshopResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KranzenzoApp.class)
@Import(DummyS3Configuration.class)
public class WorkshopResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NEEDED_MATERIALS = "AAAAAAAAAA";
    private static final String UPDATED_NEEDED_MATERIALS = "BBBBBBBBBB";

    private static final String DEFAULT_INCLUDED_IN_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_INCLUDED_IN_PRICE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION_IN_MINUTES = 1;
    private static final Integer UPDATED_DURATION_IN_MINUTES = 2;

    private static final Float DEFAULT_PRICE = 0F;
    private static final Float UPDATED_PRICE = 1F;

    private static final Integer DEFAULT_MAX_SUBSCRIPTIONS = 1;
    private static final Integer UPDATED_MAX_SUBSCRIPTIONS = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_SHOW_ON_HOMEPAGE = false;
    private static final Boolean UPDATED_SHOW_ON_HOMEPAGE = true;

    @Autowired
    private WorkshopRepository workshopRepository;

    @Mock
    private WorkshopRepository workshopRepositoryMock;

    @Autowired
    private WorkshopMapper workshopMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private WorkshopDateRepository workshopDateRepository;
    @Autowired
    private WorkshopSubscriptionRepository workshopSubscriptionRepository;

    private MockMvc restWorkshopMockMvc;

    private Workshop workshop;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workshop createEntity( EntityManager em ) {
        Workshop workshop = new Workshop()
            .name( DEFAULT_NAME )
            .description( DEFAULT_DESCRIPTION )
            .neededMaterials( DEFAULT_NEEDED_MATERIALS )
            .includedInPrice( DEFAULT_INCLUDED_IN_PRICE )
            .durationInMinutes( DEFAULT_DURATION_IN_MINUTES )
            .price( DEFAULT_PRICE )
            .maxSubscriptions( DEFAULT_MAX_SUBSCRIPTIONS )
            .isActive( DEFAULT_IS_ACTIVE )
            .showOnHomepage( DEFAULT_SHOW_ON_HOMEPAGE );
        return workshop;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks( this );
        final WorkshopResource workshopResource = new WorkshopResource( workshopRepository, workshopMapper, imageRepository, workshopDateRepository, workshopSubscriptionRepository );
        this.restWorkshopMockMvc = MockMvcBuilders.standaloneSetup( workshopResource )
                                                  .setCustomArgumentResolvers( pageableArgumentResolver )
                                                  .setControllerAdvice( exceptionTranslator )
                                                  .setConversionService( createFormattingConversionService() )
                                                  .setMessageConverters( jacksonMessageConverter ).build();
    }

    @Before
    public void initTest() {
        workshop = createEntity( em );
    }

    @Test
    @Transactional
    public void createWorkshop() throws Exception {
        int databaseSizeBeforeCreate = workshopRepository.findAll().size();

        // Create the Workshop
        WorkshopDTO workshopDTO = workshopMapper.toDto( workshop );
        restWorkshopMockMvc.perform( post( "/api/workshops" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDTO ) ) )
                           .andExpect( status().isCreated() );

        // Validate the Workshop in the database
        List <Workshop> workshopList = workshopRepository.findAll();
        assertThat( workshopList ).hasSize( databaseSizeBeforeCreate + 1 );
        Workshop testWorkshop = workshopList.get( workshopList.size() - 1 );
        assertThat( testWorkshop.getName() ).isEqualTo( DEFAULT_NAME );
        assertThat( testWorkshop.getDescription() ).isEqualTo( DEFAULT_DESCRIPTION );
        assertThat( testWorkshop.getNeededMaterials() ).isEqualTo( DEFAULT_NEEDED_MATERIALS );
        assertThat( testWorkshop.getIncludedInPrice() ).isEqualTo( DEFAULT_INCLUDED_IN_PRICE );
        assertThat( testWorkshop.getDurationInMinutes() ).isEqualTo( DEFAULT_DURATION_IN_MINUTES );
        assertThat( testWorkshop.getPrice() ).isEqualTo( DEFAULT_PRICE );
        assertThat( testWorkshop.getMaxSubscriptions() ).isEqualTo( DEFAULT_MAX_SUBSCRIPTIONS );
        assertThat( testWorkshop.isIsActive() ).isEqualTo( DEFAULT_IS_ACTIVE );
        assertThat( testWorkshop.isShowOnHomepage() ).isEqualTo( DEFAULT_SHOW_ON_HOMEPAGE );
    }

    @Test
    @Transactional
    public void createWorkshopWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workshopRepository.findAll().size();

        // Create the Workshop with an existing ID
        workshop.setId( 1L );
        WorkshopDTO workshopDTO = workshopMapper.toDto( workshop );

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkshopMockMvc.perform( post( "/api/workshops" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDTO ) ) )
                           .andExpect( status().isBadRequest() );

        // Validate the Workshop in the database
        List <Workshop> workshopList = workshopRepository.findAll();
        assertThat( workshopList ).hasSize( databaseSizeBeforeCreate );
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workshopRepository.findAll().size();
        // set the field null
        workshop.setName( null );

        // Create the Workshop, which fails.
        WorkshopDTO workshopDTO = workshopMapper.toDto( workshop );

        restWorkshopMockMvc.perform( post( "/api/workshops" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDTO ) ) )
                           .andExpect( status().isBadRequest() );

        List <Workshop> workshopList = workshopRepository.findAll();
        assertThat( workshopList ).hasSize( databaseSizeBeforeTest );
    }

    @Test
    @Transactional
    public void checkDurationInMinutesIsRequired() throws Exception {
        int databaseSizeBeforeTest = workshopRepository.findAll().size();
        // set the field null
        workshop.setDurationInMinutes( null );

        // Create the Workshop, which fails.
        WorkshopDTO workshopDTO = workshopMapper.toDto( workshop );

        restWorkshopMockMvc.perform( post( "/api/workshops" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDTO ) ) )
                           .andExpect( status().isBadRequest() );

        List <Workshop> workshopList = workshopRepository.findAll();
        assertThat( workshopList ).hasSize( databaseSizeBeforeTest );
    }

    @Test
    @Transactional
    public void getAllWorkshops() throws Exception {
        // Initialize the database
        workshopRepository.saveAndFlush( workshop );

        // Get all the workshopList
        restWorkshopMockMvc.perform( get( "/api/workshops?sort=id,desc" ) )
                           .andExpect( status().isOk() )
                           .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                           .andExpect( jsonPath( "$.[*].id" ).value( hasItem( workshop.getId().intValue() ) ) )
                           .andExpect( jsonPath( "$.[*].name" ).value( hasItem( DEFAULT_NAME.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].description" )
                               .value( hasItem( DEFAULT_DESCRIPTION.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].neededMaterials" )
                               .value( hasItem( DEFAULT_NEEDED_MATERIALS.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].includedInPrice" )
                               .value( hasItem( DEFAULT_INCLUDED_IN_PRICE.toString() ) ) )
                           .andExpect( jsonPath( "$.[*].durationInMinutes" )
                               .value( hasItem( DEFAULT_DURATION_IN_MINUTES ) ) )
                           .andExpect( jsonPath( "$.[*].price" ).value( hasItem( DEFAULT_PRICE.doubleValue() ) ) )
                           .andExpect( jsonPath( "$.[*].maxSubscriptions" )
                               .value( hasItem( DEFAULT_MAX_SUBSCRIPTIONS ) ) )
                           .andExpect( jsonPath( "$.[*].isActive" )
                               .value( hasItem( DEFAULT_IS_ACTIVE.booleanValue() ) ) )
                           .andExpect( jsonPath( "$.[*].showOnHomepage" )
                               .value( hasItem( DEFAULT_SHOW_ON_HOMEPAGE.booleanValue() ) ) );
    }

    public void getAllWorkshopsWithEagerRelationshipsIsEnabled() throws Exception {
        WorkshopResource workshopResource = new WorkshopResource( workshopRepositoryMock, workshopMapper, imageRepository, workshopDateRepository, workshopSubscriptionRepository );
        when( workshopRepositoryMock.findAllWithEagerRelationships( any() ) )
            .thenReturn( new PageImpl( new ArrayList <>() ) );

        MockMvc restWorkshopMockMvc = MockMvcBuilders.standaloneSetup( workshopResource )
                                                     .setCustomArgumentResolvers( pageableArgumentResolver )
                                                     .setControllerAdvice( exceptionTranslator )
                                                     .setConversionService( createFormattingConversionService() )
                                                     .setMessageConverters( jacksonMessageConverter ).build();

        restWorkshopMockMvc.perform( get( "/api/workshops?eagerload=true" ) )
                           .andExpect( status().isOk() );

        verify( workshopRepositoryMock, times( 1 ) ).findAllWithEagerRelationships( any() );
    }

    public void getAllWorkshopsWithEagerRelationshipsIsNotEnabled() throws Exception {
        WorkshopResource workshopResource = new WorkshopResource( workshopRepositoryMock, workshopMapper, imageRepository, workshopDateRepository, workshopSubscriptionRepository );
        when( workshopRepositoryMock.findAllWithEagerRelationships( any() ) )
            .thenReturn( new PageImpl( new ArrayList <>() ) );
        MockMvc restWorkshopMockMvc = MockMvcBuilders.standaloneSetup( workshopResource )
                                                     .setCustomArgumentResolvers( pageableArgumentResolver )
                                                     .setControllerAdvice( exceptionTranslator )
                                                     .setConversionService( createFormattingConversionService() )
                                                     .setMessageConverters( jacksonMessageConverter ).build();

        restWorkshopMockMvc.perform( get( "/api/workshops?eagerload=true" ) )
                           .andExpect( status().isOk() );

        verify( workshopRepositoryMock, times( 1 ) ).findAllWithEagerRelationships( any() );
    }

    @Test
    @Transactional
    public void getWorkshop() throws Exception {
        // Initialize the database
        workshopRepository.saveAndFlush( workshop );

        // Get the workshop
        restWorkshopMockMvc.perform( get( "/api/workshops/{id}", workshop.getId() ) )
                           .andExpect( status().isOk() )
                           .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                           .andExpect( jsonPath( "$.id" ).value( workshop.getId().intValue() ) )
                           .andExpect( jsonPath( "$.name" ).value( DEFAULT_NAME.toString() ) )
                           .andExpect( jsonPath( "$.description" ).value( DEFAULT_DESCRIPTION.toString() ) )
                           .andExpect( jsonPath( "$.neededMaterials" ).value( DEFAULT_NEEDED_MATERIALS.toString() ) )
                           .andExpect( jsonPath( "$.includedInPrice" ).value( DEFAULT_INCLUDED_IN_PRICE.toString() ) )
                           .andExpect( jsonPath( "$.durationInMinutes" ).value( DEFAULT_DURATION_IN_MINUTES ) )
                           .andExpect( jsonPath( "$.price" ).value( DEFAULT_PRICE.doubleValue() ) )
                           .andExpect( jsonPath( "$.maxSubscriptions" ).value( DEFAULT_MAX_SUBSCRIPTIONS ) )
                           .andExpect( jsonPath( "$.isActive" ).value( DEFAULT_IS_ACTIVE.booleanValue() ) )
                           .andExpect( jsonPath( "$.showOnHomepage" )
                               .value( DEFAULT_SHOW_ON_HOMEPAGE.booleanValue() ) );
    }

    @Test
    @Transactional
    public void getNonExistingWorkshop() throws Exception {
        // Get the workshop
        restWorkshopMockMvc.perform( get( "/api/workshops/{id}", Long.MAX_VALUE ) )
                           .andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void updateWorkshop() throws Exception {
        // Initialize the database
        workshopRepository.saveAndFlush( workshop );

        int databaseSizeBeforeUpdate = workshopRepository.findAll().size();

        // Update the workshop
        Workshop updatedWorkshop = workshopRepository.findById( workshop.getId() ).get();
        // Disconnect from session so that the updates on updatedWorkshop are not directly saved in db
        em.detach( updatedWorkshop );
        updatedWorkshop
            .name( UPDATED_NAME )
            .description( UPDATED_DESCRIPTION )
            .neededMaterials( UPDATED_NEEDED_MATERIALS )
            .includedInPrice( UPDATED_INCLUDED_IN_PRICE )
            .durationInMinutes( UPDATED_DURATION_IN_MINUTES )
            .price( UPDATED_PRICE )
            .maxSubscriptions( UPDATED_MAX_SUBSCRIPTIONS )
            .isActive( UPDATED_IS_ACTIVE )
            .showOnHomepage( UPDATED_SHOW_ON_HOMEPAGE );
        WorkshopDTO workshopDTO = workshopMapper.toDto( updatedWorkshop );

        restWorkshopMockMvc.perform( put( "/api/workshops" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDTO ) ) )
                           .andExpect( status().isOk() );

        // Validate the Workshop in the database
        List <Workshop> workshopList = workshopRepository.findAll();
        assertThat( workshopList ).hasSize( databaseSizeBeforeUpdate );
        Workshop testWorkshop = workshopList.get( workshopList.size() - 1 );
        assertThat( testWorkshop.getName() ).isEqualTo( UPDATED_NAME );
        assertThat( testWorkshop.getDescription() ).isEqualTo( UPDATED_DESCRIPTION );
        assertThat( testWorkshop.getNeededMaterials() ).isEqualTo( UPDATED_NEEDED_MATERIALS );
        assertThat( testWorkshop.getIncludedInPrice() ).isEqualTo( UPDATED_INCLUDED_IN_PRICE );
        assertThat( testWorkshop.getDurationInMinutes() ).isEqualTo( UPDATED_DURATION_IN_MINUTES );
        assertThat( testWorkshop.getPrice() ).isEqualTo( UPDATED_PRICE );
        assertThat( testWorkshop.getMaxSubscriptions() ).isEqualTo( UPDATED_MAX_SUBSCRIPTIONS );
        assertThat( testWorkshop.isIsActive() ).isEqualTo( UPDATED_IS_ACTIVE );
        assertThat( testWorkshop.isShowOnHomepage() ).isEqualTo( UPDATED_SHOW_ON_HOMEPAGE );
    }

    @Test
    @Transactional
    public void updateNonExistingWorkshop() throws Exception {
        int databaseSizeBeforeUpdate = workshopRepository.findAll().size();

        // Create the Workshop
        WorkshopDTO workshopDTO = workshopMapper.toDto( workshop );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkshopMockMvc.perform( put( "/api/workshops" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDTO ) ) )
                           .andExpect( status().isBadRequest() );

        // Validate the Workshop in the database
        List <Workshop> workshopList = workshopRepository.findAll();
        assertThat( workshopList ).hasSize( databaseSizeBeforeUpdate );
    }

    @Test
    @Transactional
    public void deleteWorkshop() throws Exception {
        // Initialize the database
        workshopRepository.saveAndFlush( workshop );

        int databaseSizeBeforeDelete = workshopRepository.findAll().size();

        // Get the workshop
        restWorkshopMockMvc.perform( delete( "/api/workshops/{id}", workshop.getId() )
            .accept( TestUtil.APPLICATION_JSON_UTF8 ) )
                           .andExpect( status().isOk() );

        // Validate the database is empty
        List <Workshop> workshopList = workshopRepository.findAll();
        assertThat( workshopList ).hasSize( databaseSizeBeforeDelete - 1 );
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier( Workshop.class );
        Workshop workshop1 = new Workshop();
        workshop1.setId( 1L );
        Workshop workshop2 = new Workshop();
        workshop2.setId( workshop1.getId() );
        assertThat( workshop1 ).isEqualTo( workshop2 );
        workshop2.setId( 2L );
        assertThat( workshop1 ).isNotEqualTo( workshop2 );
        workshop1.setId( null );
        assertThat( workshop1 ).isNotEqualTo( workshop2 );
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier( WorkshopDTO.class );
        WorkshopDTO workshopDTO1 = new WorkshopDTO();
        workshopDTO1.setId( 1L );
        WorkshopDTO workshopDTO2 = new WorkshopDTO();
        assertThat( workshopDTO1 ).isNotEqualTo( workshopDTO2 );
        workshopDTO2.setId( workshopDTO1.getId() );
        assertThat( workshopDTO1 ).isEqualTo( workshopDTO2 );
        workshopDTO2.setId( 2L );
        assertThat( workshopDTO1 ).isNotEqualTo( workshopDTO2 );
        workshopDTO1.setId( null );
        assertThat( workshopDTO1 ).isNotEqualTo( workshopDTO2 );
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat( workshopMapper.fromId( 42L ).getId() ).isEqualTo( 42 );
        assertThat( workshopMapper.fromId( null ) ).isNull();
    }
}
