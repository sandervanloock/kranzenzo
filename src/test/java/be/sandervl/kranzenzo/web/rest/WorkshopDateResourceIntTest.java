package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.KranzenzoApp;
import be.sandervl.kranzenzo.domain.WorkshopDate;
import be.sandervl.kranzenzo.repository.WorkshopDateRepository;
import be.sandervl.kranzenzo.service.dto.WorkshopDateDTO;
import be.sandervl.kranzenzo.service.mapper.WorkshopDateMapper;
import be.sandervl.kranzenzo.web.rest.errors.ExceptionTranslator;
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

import static be.sandervl.kranzenzo.web.rest.TestUtil.createFormattingConversionService;
import static be.sandervl.kranzenzo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WorkshopDateResource REST controller.
 *
 * @see WorkshopDateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KranzenzoApp.class)
public class WorkshopDateResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant( Instant
        .ofEpochMilli( 0L ), ZoneOffset.UTC );
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now( ZoneId.systemDefault() ).withNano( 0 );

    @Autowired
    private WorkshopDateRepository workshopDateRepository;

    @Autowired
    private WorkshopDateMapper workshopDateMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkshopDateMockMvc;

    private WorkshopDate workshopDate;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkshopDate createEntity( EntityManager em ) {
        WorkshopDate workshopDate = new WorkshopDate()
            .date( DEFAULT_DATE );
        return workshopDate;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks( this );
        final WorkshopDateResource workshopDateResource =
            new WorkshopDateResource( workshopDateRepository, workshopDateMapper );
        this.restWorkshopDateMockMvc = MockMvcBuilders.standaloneSetup( workshopDateResource )
                                                      .setCustomArgumentResolvers( pageableArgumentResolver )
                                                      .setControllerAdvice( exceptionTranslator )
                                                      .setConversionService( createFormattingConversionService() )
                                                      .setMessageConverters( jacksonMessageConverter ).build();
    }

    @Before
    public void initTest() {
        workshopDate = createEntity( em );
    }

    @Test
    @Transactional
    public void createWorkshopDate() throws Exception {
        int databaseSizeBeforeCreate = workshopDateRepository.findAll().size();

        // Create the WorkshopDate
        WorkshopDateDTO workshopDateDTO = workshopDateMapper.toDto( workshopDate );
        restWorkshopDateMockMvc.perform( post( "/api/workshop-dates" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDateDTO ) ) )
                               .andExpect( status().isCreated() );

        // Validate the WorkshopDate in the database
        List <WorkshopDate> workshopDateList = workshopDateRepository.findAll();
        assertThat( workshopDateList ).hasSize( databaseSizeBeforeCreate + 1 );
        WorkshopDate testWorkshopDate = workshopDateList.get( workshopDateList.size() - 1 );
        assertThat( testWorkshopDate.getDate() ).isEqualTo( DEFAULT_DATE );
    }

    @Test
    @Transactional
    public void createWorkshopDateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workshopDateRepository.findAll().size();

        // Create the WorkshopDate with an existing ID
        workshopDate.setId( 1L );
        WorkshopDateDTO workshopDateDTO = workshopDateMapper.toDto( workshopDate );

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkshopDateMockMvc.perform( post( "/api/workshop-dates" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDateDTO ) ) )
                               .andExpect( status().isBadRequest() );

        // Validate the WorkshopDate in the database
        List <WorkshopDate> workshopDateList = workshopDateRepository.findAll();
        assertThat( workshopDateList ).hasSize( databaseSizeBeforeCreate );
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = workshopDateRepository.findAll().size();
        // set the field null
        workshopDate.setDate( null );

        // Create the WorkshopDate, which fails.
        WorkshopDateDTO workshopDateDTO = workshopDateMapper.toDto( workshopDate );

        restWorkshopDateMockMvc.perform( post( "/api/workshop-dates" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDateDTO ) ) )
                               .andExpect( status().isBadRequest() );

        List <WorkshopDate> workshopDateList = workshopDateRepository.findAll();
        assertThat( workshopDateList ).hasSize( databaseSizeBeforeTest );
    }

    @Test
    @Transactional
    public void getAllWorkshopDates() throws Exception {
        // Initialize the database
        workshopDateRepository.saveAndFlush( workshopDate );

        // Get all the workshopDateList
        restWorkshopDateMockMvc.perform( get( "/api/workshop-dates?sort=id,desc" ) )
                               .andExpect( status().isOk() )
                               .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                               .andExpect( jsonPath( "$.[*].id" ).value( hasItem( workshopDate.getId().intValue() ) ) )
                               .andExpect( jsonPath( "$.[*].date" ).value( hasItem( sameInstant( DEFAULT_DATE ) ) ) );
    }

    @Test
    @Transactional
    public void getWorkshopDate() throws Exception {
        // Initialize the database
        workshopDateRepository.saveAndFlush( workshopDate );

        // Get the workshopDate
        restWorkshopDateMockMvc.perform( get( "/api/workshop-dates/{id}", workshopDate.getId() ) )
                               .andExpect( status().isOk() )
                               .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
                               .andExpect( jsonPath( "$.id" ).value( workshopDate.getId().intValue() ) )
                               .andExpect( jsonPath( "$.date" ).value( sameInstant( DEFAULT_DATE ) ) );
    }

    @Test
    @Transactional
    public void getNonExistingWorkshopDate() throws Exception {
        // Get the workshopDate
        restWorkshopDateMockMvc.perform( get( "/api/workshop-dates/{id}", Long.MAX_VALUE ) )
                               .andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void updateWorkshopDate() throws Exception {
        // Initialize the database
        workshopDateRepository.saveAndFlush( workshopDate );

        int databaseSizeBeforeUpdate = workshopDateRepository.findAll().size();

        // Update the workshopDate
        WorkshopDate updatedWorkshopDate = workshopDateRepository.findById( workshopDate.getId() ).get();
        // Disconnect from session so that the updates on updatedWorkshopDate are not directly saved in db
        em.detach( updatedWorkshopDate );
        updatedWorkshopDate
            .date( UPDATED_DATE );
        WorkshopDateDTO workshopDateDTO = workshopDateMapper.toDto( updatedWorkshopDate );

        restWorkshopDateMockMvc.perform( put( "/api/workshop-dates" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDateDTO ) ) )
                               .andExpect( status().isOk() );

        // Validate the WorkshopDate in the database
        List <WorkshopDate> workshopDateList = workshopDateRepository.findAll();
        assertThat( workshopDateList ).hasSize( databaseSizeBeforeUpdate );
        WorkshopDate testWorkshopDate = workshopDateList.get( workshopDateList.size() - 1 );
        assertThat( testWorkshopDate.getDate() ).isEqualTo( UPDATED_DATE );
    }

    @Test
    @Transactional
    public void updateNonExistingWorkshopDate() throws Exception {
        int databaseSizeBeforeUpdate = workshopDateRepository.findAll().size();

        // Create the WorkshopDate
        WorkshopDateDTO workshopDateDTO = workshopDateMapper.toDto( workshopDate );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkshopDateMockMvc.perform( put( "/api/workshop-dates" )
            .contentType( TestUtil.APPLICATION_JSON_UTF8 )
            .content( TestUtil.convertObjectToJsonBytes( workshopDateDTO ) ) )
                               .andExpect( status().isBadRequest() );

        // Validate the WorkshopDate in the database
        List <WorkshopDate> workshopDateList = workshopDateRepository.findAll();
        assertThat( workshopDateList ).hasSize( databaseSizeBeforeUpdate );
    }

    @Test
    @Transactional
    public void deleteWorkshopDate() throws Exception {
        // Initialize the database
        workshopDateRepository.saveAndFlush( workshopDate );

        int databaseSizeBeforeDelete = workshopDateRepository.findAll().size();

        // Get the workshopDate
        restWorkshopDateMockMvc.perform( delete( "/api/workshop-dates/{id}", workshopDate.getId() )
            .accept( TestUtil.APPLICATION_JSON_UTF8 ) )
                               .andExpect( status().isOk() );

        // Validate the database is empty
        List <WorkshopDate> workshopDateList = workshopDateRepository.findAll();
        assertThat( workshopDateList ).hasSize( databaseSizeBeforeDelete - 1 );
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier( WorkshopDate.class );
        WorkshopDate workshopDate1 = new WorkshopDate();
        workshopDate1.setId( 1L );
        WorkshopDate workshopDate2 = new WorkshopDate();
        workshopDate2.setId( workshopDate1.getId() );
        assertThat( workshopDate1 ).isEqualTo( workshopDate2 );
        workshopDate2.setId( 2L );
        assertThat( workshopDate1 ).isNotEqualTo( workshopDate2 );
        workshopDate1.setId( null );
        assertThat( workshopDate1 ).isNotEqualTo( workshopDate2 );
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier( WorkshopDateDTO.class );
        WorkshopDateDTO workshopDateDTO1 = new WorkshopDateDTO();
        workshopDateDTO1.setId( 1L );
        WorkshopDateDTO workshopDateDTO2 = new WorkshopDateDTO();
        assertThat( workshopDateDTO1 ).isNotEqualTo( workshopDateDTO2 );
        workshopDateDTO2.setId( workshopDateDTO1.getId() );
        assertThat( workshopDateDTO1 ).isEqualTo( workshopDateDTO2 );
        workshopDateDTO2.setId( 2L );
        assertThat( workshopDateDTO1 ).isNotEqualTo( workshopDateDTO2 );
        workshopDateDTO1.setId( null );
        assertThat( workshopDateDTO1 ).isNotEqualTo( workshopDateDTO2 );
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat( workshopDateMapper.fromId( 42L ).getId() ).isEqualTo( 42 );
        assertThat( workshopDateMapper.fromId( null ) ).isNull();
    }
}
