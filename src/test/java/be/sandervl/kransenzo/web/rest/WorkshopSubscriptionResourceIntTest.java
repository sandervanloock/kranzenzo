package be.sandervl.kransenzo.web.rest;

import be.sandervl.kransenzo.KransenzoApp;
import be.sandervl.kransenzo.domain.User;
import be.sandervl.kransenzo.domain.Workshop;
import be.sandervl.kransenzo.domain.WorkshopDate;
import be.sandervl.kransenzo.domain.WorkshopSubscription;
import be.sandervl.kransenzo.domain.enumeration.SubscriptionState;
import be.sandervl.kransenzo.repository.UserRepository;
import be.sandervl.kransenzo.repository.WorkshopDateRepository;
import be.sandervl.kransenzo.repository.WorkshopRepository;
import be.sandervl.kransenzo.repository.WorkshopSubscriptionRepository;
import be.sandervl.kransenzo.repository.search.WorkshopSubscriptionSearchRepository;
import be.sandervl.kransenzo.service.MailService;
import be.sandervl.kransenzo.service.UserService;
import be.sandervl.kransenzo.service.dto.WorkshopSubscriptionDTO;
import be.sandervl.kransenzo.service.mapper.WorkshopSubscriptionMapper;
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

import static be.sandervl.kransenzo.web.rest.TestUtil.createFormattingConversionService;
import static be.sandervl.kransenzo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WorkshopSubscriptionResource REST controller.
 *
 * @see WorkshopSubscriptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KransenzoApp.class)
public class WorkshopSubscriptionResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant( Instant
        .ofEpochMilli( 0L ), ZoneOffset.UTC );
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now( ZoneId.systemDefault() ).withNano( 0 );

    private static final SubscriptionState DEFAULT_STATE = SubscriptionState.NEW;
    private static final SubscriptionState UPDATED_STATE = SubscriptionState.PAYED;

    @Autowired
    private WorkshopSubscriptionRepository workshopSubscriptionRepository;

    @Autowired
    private WorkshopSubscriptionMapper workshopSubscriptionMapper;

    @Autowired
    private WorkshopSubscriptionSearchRepository workshopSubscriptionSearchRepository;

    @Autowired
    private UserService userService;

    private static User USER;

    @Autowired
    private MailService mailService;

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private WorkshopDateRepository workshopDateRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkshopSubscriptionMockMvc;

    private WorkshopSubscription workshopSubscription;
    private static WorkshopDate WORKSHOP_DATE;
    private static Workshop WORKSHOP;
    @Autowired
    private UserRepository userRepository;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkshopSubscription createEntity( EntityManager em ) {
        WorkshopSubscription workshopSubscription = new WorkshopSubscription()
            .created( DEFAULT_CREATED )
            .state( DEFAULT_STATE );
        return workshopSubscription;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks( this );
        final WorkshopSubscriptionResource workshopSubscriptionResource =
            new WorkshopSubscriptionResource( workshopSubscriptionRepository, workshopSubscriptionMapper, workshopSubscriptionSearchRepository, userService, mailService, workshopDateRepository, workshopRepository );
        this.restWorkshopSubscriptionMockMvc = MockMvcBuilders.standaloneSetup( workshopSubscriptionResource )
                                                              .setCustomArgumentResolvers( pageableArgumentResolver )
                                                              .setControllerAdvice( exceptionTranslator )
                                                              .setConversionService( createFormattingConversionService() )
                                                              .setMessageConverters( jacksonMessageConverter ).build();
    }

    @Before
    public void initTest() {
        workshopSubscriptionSearchRepository.deleteAll();
        workshopSubscription = createEntity( em );
        USER = UserResourceIntTest.createEntity( em );
        USER.setLangKey( "nl" );
        userRepository.saveAndFlush( USER );
        workshopSubscription.setUser( USER );
        WORKSHOP_DATE = WorkshopDateResourceIntTest.createEntity( em );
        workshopDateRepository.saveAndFlush( WORKSHOP_DATE );
        WORKSHOP = WorkshopResourceIntTest.createEntity( em );
        workshopRepository.saveAndFlush( WORKSHOP );
        WORKSHOP_DATE.setWorkshop( WORKSHOP );
        workshopSubscription.setWorkshop( WORKSHOP_DATE );
    }

    @Test
    @Transactional
    public void createWorkshopSubscription() throws Exception {
        int databaseSizeBeforeCreate = workshopSubscriptionRepository.findAll().size();

        // Create the WorkshopSubscription
        WorkshopSubscriptionDTO workshopSubscriptionDTO = workshopSubscriptionMapper.toDto( workshopSubscription );
        restWorkshopSubscriptionMockMvc.perform( post( "/api/workshop-subscriptions" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopSubscriptionDTO ) ) )
                                       .andExpect( status().isCreated() );

        // Validate the WorkshopSubscription in the database
        List <WorkshopSubscription> workshopSubscriptionList = workshopSubscriptionRepository.findAll();
        assertThat( workshopSubscriptionList ).hasSize( databaseSizeBeforeCreate + 1 );
        WorkshopSubscription testWorkshopSubscription = workshopSubscriptionList.get( workshopSubscriptionList
            .size() - 1 );
        //assertThat( testWorkshopSubscription.getCreated() ).isEqualTo( DEFAULT_CREATED );
        assertThat( testWorkshopSubscription.getState() ).isEqualTo( DEFAULT_STATE );

        // Validate the WorkshopSubscription in Elasticsearch
        //WorkshopSubscription workshopSubscriptionEs =
        //    workshopSubscriptionSearchRepository.findOne( testWorkshopSubscription.getId() );
        //assertThat( workshopSubscriptionEs ).isEqualToComparingFieldByField( testWorkshopSubscription );
    }

    @Test
    @Transactional
    public void createWorkshopSubscriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workshopSubscriptionRepository.findAll().size();

        // Create the WorkshopSubscription with an existing ID
        workshopSubscription.setId( 1L );
        WorkshopSubscriptionDTO workshopSubscriptionDTO = workshopSubscriptionMapper.toDto( workshopSubscription );

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkshopSubscriptionMockMvc.perform( post( "/api/workshop-subscriptions" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopSubscriptionDTO ) ) )
                                       .andExpect( status().isBadRequest() );

        // Validate the WorkshopSubscription in the database
        List <WorkshopSubscription> workshopSubscriptionList = workshopSubscriptionRepository.findAll();
        assertThat( workshopSubscriptionList ).hasSize( databaseSizeBeforeCreate );
    }

    @Test
    @Transactional
    public void getAllWorkshopSubscriptions() throws Exception {
        // Initialize the database
        workshopSubscriptionRepository.saveAndFlush( workshopSubscription );

        // Get all the workshopSubscriptionList
        restWorkshopSubscriptionMockMvc.perform( get( "/api/workshop-subscriptions?sort=id,desc" ) )
                                       .andExpect( status().isOk() )
                                       .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                                       .andExpect( jsonPath( "$.[*].id" )
                                           .value( hasItem( workshopSubscription.getId().intValue() ) ) )
                                       .andExpect( jsonPath( "$.[*].created" )
                                           .value( hasItem( sameInstant( DEFAULT_CREATED ) ) ) )
                                       .andExpect( jsonPath( "$.[*].state" )
                                           .value( hasItem( DEFAULT_STATE.toString() ) ) );
    }

    @Test
    @Transactional
    public void getWorkshopSubscription() throws Exception {
        // Initialize the database
        workshopSubscriptionRepository.saveAndFlush( workshopSubscription );

        // Get the workshopSubscription
        restWorkshopSubscriptionMockMvc.perform( get( "/api/workshop-subscriptions/{id}", workshopSubscription
            .getId() ) )
                                       .andExpect( status().isOk() )
                                       .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                                       .andExpect( jsonPath( "$.id" ).value( workshopSubscription.getId().intValue() ) )
                                       .andExpect( jsonPath( "$.created" ).value( sameInstant( DEFAULT_CREATED ) ) )
                                       .andExpect( jsonPath( "$.state" ).value( DEFAULT_STATE.toString() ) );
    }

    @Test
    @Transactional
    public void getNonExistingWorkshopSubscription() throws Exception {
        // Get the workshopSubscription
        restWorkshopSubscriptionMockMvc.perform( get( "/api/workshop-subscriptions/{id}", Long.MAX_VALUE ) )
                                       .andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void updateWorkshopSubscription() throws Exception {
        // Initialize the database
        workshopSubscriptionRepository.saveAndFlush( workshopSubscription );
        workshopSubscriptionSearchRepository.save( workshopSubscription );
        int databaseSizeBeforeUpdate = workshopSubscriptionRepository.findAll().size();

        // Update the workshopSubscription
        WorkshopSubscription updatedWorkshopSubscription = workshopSubscriptionRepository.findOne( workshopSubscription
            .getId() );
        // Disconnect from session so that the updates on updatedWorkshopSubscription are not directly saved in db
        em.detach( updatedWorkshopSubscription );
        updatedWorkshopSubscription
            .created( UPDATED_CREATED )
            .state( UPDATED_STATE );
        WorkshopSubscriptionDTO workshopSubscriptionDTO =
            workshopSubscriptionMapper.toDto( updatedWorkshopSubscription );

        restWorkshopSubscriptionMockMvc.perform( put( "/api/workshop-subscriptions" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopSubscriptionDTO ) ) )
                                       .andExpect( status().isOk() );

        // Validate the WorkshopSubscription in the database
        List <WorkshopSubscription> workshopSubscriptionList = workshopSubscriptionRepository.findAll();
        assertThat( workshopSubscriptionList ).hasSize( databaseSizeBeforeUpdate );
        WorkshopSubscription testWorkshopSubscription = workshopSubscriptionList.get( workshopSubscriptionList
            .size() - 1 );
        assertThat( testWorkshopSubscription.getCreated() ).isEqualTo( UPDATED_CREATED );
        assertThat( testWorkshopSubscription.getState() ).isEqualTo( UPDATED_STATE );

        // Validate the WorkshopSubscription in Elasticsearch
        //WorkshopSubscription workshopSubscriptionEs =
        //    workshopSubscriptionSearchRepository.findOne( testWorkshopSubscription.getId() );
        //assertThat( workshopSubscriptionEs ).isEqualToComparingFieldByField( testWorkshopSubscription );
    }

    @Test
    @Transactional
    public void updateNonExistingWorkshopSubscription() throws Exception {
        int databaseSizeBeforeUpdate = workshopSubscriptionRepository.findAll().size();

        // Create the WorkshopSubscription
        WorkshopSubscriptionDTO workshopSubscriptionDTO = workshopSubscriptionMapper.toDto( workshopSubscription );

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWorkshopSubscriptionMockMvc.perform( put( "/api/workshop-subscriptions" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopSubscriptionDTO ) ) )
                                       .andExpect( status().isCreated() );

        // Validate the WorkshopSubscription in the database
        List <WorkshopSubscription> workshopSubscriptionList = workshopSubscriptionRepository.findAll();
        assertThat( workshopSubscriptionList ).hasSize( databaseSizeBeforeUpdate + 1 );
    }

    @Test
    @Transactional
    public void deleteWorkshopSubscription() throws Exception {
        // Initialize the database
        workshopSubscriptionRepository.saveAndFlush( workshopSubscription );
        workshopSubscriptionSearchRepository.save( workshopSubscription );
        int databaseSizeBeforeDelete = workshopSubscriptionRepository.findAll().size();

        // Get the workshopSubscription
        restWorkshopSubscriptionMockMvc.perform( delete( "/api/workshop-subscriptions/{id}", workshopSubscription
            .getId() )
            .accept( TestUtil.APPLICATION_JSON_UTF8 ) )
                                       .andExpect( status().isOk() );

        // Validate Elasticsearch is empty
        //boolean workshopSubscriptionExistsInEs = workshopSubscriptionSearchRepository.exists( workshopSubscription
        //    .getId() );
        //assertThat( workshopSubscriptionExistsInEs ).isFalse();

        // Validate the database is empty
        List <WorkshopSubscription> workshopSubscriptionList = workshopSubscriptionRepository.findAll();
        assertThat( workshopSubscriptionList ).hasSize( databaseSizeBeforeDelete - 1 );
    }

    @Test
    @Transactional
    public void searchWorkshopSubscription() throws Exception {
        // Initialize the database
        workshopSubscriptionRepository.saveAndFlush( workshopSubscription );
        workshopSubscriptionSearchRepository.save( workshopSubscription );

        // Search the workshopSubscription
        restWorkshopSubscriptionMockMvc
            .perform( get( "/api/_search/workshop-subscriptions?query=id:" + workshopSubscription.getId() ) )
            .andExpect( status().isOk() )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
            .andExpect( jsonPath( "$.[*].id" ).value( hasItem( workshopSubscription.getId().intValue() ) ) )
            .andExpect( jsonPath( "$.[*].created" ).value( hasItem( sameInstant( DEFAULT_CREATED ) ) ) )
            .andExpect( jsonPath( "$.[*].state" ).value( hasItem( DEFAULT_STATE.toString() ) ) );
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier( WorkshopSubscription.class );
        WorkshopSubscription workshopSubscription1 = new WorkshopSubscription();
        workshopSubscription1.setId( 1L );
        WorkshopSubscription workshopSubscription2 = new WorkshopSubscription();
        workshopSubscription2.setId( workshopSubscription1.getId() );
        assertThat( workshopSubscription1 ).isEqualTo( workshopSubscription2 );
        workshopSubscription2.setId( 2L );
        assertThat( workshopSubscription1 ).isNotEqualTo( workshopSubscription2 );
        workshopSubscription1.setId( null );
        assertThat( workshopSubscription1 ).isNotEqualTo( workshopSubscription2 );
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier( WorkshopSubscriptionDTO.class );
        WorkshopSubscriptionDTO workshopSubscriptionDTO1 = new WorkshopSubscriptionDTO();
        workshopSubscriptionDTO1.setId( 1L );
        WorkshopSubscriptionDTO workshopSubscriptionDTO2 = new WorkshopSubscriptionDTO();
        assertThat( workshopSubscriptionDTO1 ).isNotEqualTo( workshopSubscriptionDTO2 );
        workshopSubscriptionDTO2.setId( workshopSubscriptionDTO1.getId() );
        assertThat( workshopSubscriptionDTO1 ).isEqualTo( workshopSubscriptionDTO2 );
        workshopSubscriptionDTO2.setId( 2L );
        assertThat( workshopSubscriptionDTO1 ).isNotEqualTo( workshopSubscriptionDTO2 );
        workshopSubscriptionDTO1.setId( null );
        assertThat( workshopSubscriptionDTO1 ).isNotEqualTo( workshopSubscriptionDTO2 );
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat( workshopSubscriptionMapper.fromId( 42L ).getId() ).isEqualTo( 42 );
        assertThat( workshopSubscriptionMapper.fromId( null ) ).isNull();
    }
}
