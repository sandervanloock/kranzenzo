package be.sandervl.kransenzo.web.rest;

import be.sandervl.kransenzo.KransenzoApp;
import be.sandervl.kransenzo.domain.Location;
import be.sandervl.kransenzo.repository.LocationRepository;
import be.sandervl.kransenzo.repository.search.LocationSearchRepository;
import be.sandervl.kransenzo.service.dto.LocationDTO;
import be.sandervl.kransenzo.service.mapper.LocationMapper;
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
 * Test class for the LocationResource REST controller.
 *
 * @see LocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KransenzoApp.class)
public class LocationResourceIntTest
{

	private static final Float DEFAULT_LATITUDE = -90F;
	private static final Float UPDATED_LATITUDE = -89F;

	private static final Float DEFAULT_LONGITUDE = -180F;
	private static final Float UPDATED_LONGITUDE = -179F;

	private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
	private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private LocationMapper locationMapper;

	@Autowired
	private LocationSearchRepository locationSearchRepository;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restLocationMockMvc;

	private Location location;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Location createEntity( EntityManager em ) {
		Location location = new Location()
				.latitude( DEFAULT_LATITUDE )
				.longitude( DEFAULT_LONGITUDE )
				.description( DEFAULT_DESCRIPTION );
		return location;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
		LocationResource locationResource = new LocationResource( locationRepository, locationMapper,
		                                                          locationSearchRepository );
		this.restLocationMockMvc = MockMvcBuilders.standaloneSetup( locationResource )
		                                          .setCustomArgumentResolvers( pageableArgumentResolver )
		                                          .setControllerAdvice( exceptionTranslator )
		                                          .setMessageConverters( jacksonMessageConverter ).build();
	}

	@Before
	public void initTest() {
		locationSearchRepository.deleteAll();
		location = createEntity( em );
	}

	@Test
	@Transactional
	public void createLocation() throws Exception {
		int databaseSizeBeforeCreate = locationRepository.findAll().size();

		// Create the Location
		LocationDTO locationDTO = locationMapper.toDto( location );
		restLocationMockMvc.perform( post( "/api/locations" )
				                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
				                             .content( TestUtil.convertObjectToJsonBytes( locationDTO ) ) )
		                   .andExpect( status().isCreated() );

		// Validate the Location in the database
		List<Location> locationList = locationRepository.findAll();
		assertThat( locationList ).hasSize( databaseSizeBeforeCreate + 1 );
		Location testLocation = locationList.get( locationList.size() - 1 );
		assertThat( testLocation.getLatitude() ).isEqualTo( DEFAULT_LATITUDE );
		assertThat( testLocation.getLongitude() ).isEqualTo( DEFAULT_LONGITUDE );
		assertThat( testLocation.getDescription() ).isEqualTo( DEFAULT_DESCRIPTION );

		// Validate the Location in Elasticsearch
		Location locationEs = locationSearchRepository.findOne( testLocation.getId() );
		assertThat( locationEs ).isEqualToComparingFieldByField( testLocation );
	}

	@Test
	@Transactional
	public void createLocationWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = locationRepository.findAll().size();

		// Create the Location with an existing ID
		location.setId( 1L );
		LocationDTO locationDTO = locationMapper.toDto( location );

		// An entity with an existing ID cannot be created, so this API call must fail
		restLocationMockMvc.perform( post( "/api/locations" )
				                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
				                             .content( TestUtil.convertObjectToJsonBytes( locationDTO ) ) )
		                   .andExpect( status().isBadRequest() );

		// Validate the Alice in the database
		List<Location> locationList = locationRepository.findAll();
		assertThat( locationList ).hasSize( databaseSizeBeforeCreate );
	}

	@Test
	@Transactional
	public void checkLatitudeIsRequired() throws Exception {
		int databaseSizeBeforeTest = locationRepository.findAll().size();
		// set the field null
		location.setLatitude( null );

		// Create the Location, which fails.
		LocationDTO locationDTO = locationMapper.toDto( location );

		restLocationMockMvc.perform( post( "/api/locations" )
				                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
				                             .content( TestUtil.convertObjectToJsonBytes( locationDTO ) ) )
		                   .andExpect( status().isBadRequest() );

		List<Location> locationList = locationRepository.findAll();
		assertThat( locationList ).hasSize( databaseSizeBeforeTest );
	}

	@Test
	@Transactional
	public void checkLongitudeIsRequired() throws Exception {
		int databaseSizeBeforeTest = locationRepository.findAll().size();
		// set the field null
		location.setLongitude( null );

		// Create the Location, which fails.
		LocationDTO locationDTO = locationMapper.toDto( location );

		restLocationMockMvc.perform( post( "/api/locations" )
				                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
				                             .content( TestUtil.convertObjectToJsonBytes( locationDTO ) ) )
		                   .andExpect( status().isBadRequest() );

		List<Location> locationList = locationRepository.findAll();
		assertThat( locationList ).hasSize( databaseSizeBeforeTest );
	}

	@Test
	@Transactional
	public void getAllLocations() throws Exception {
		// Initialize the database
		locationRepository.saveAndFlush( location );

		// Get all the locationList
		restLocationMockMvc.perform( get( "/api/locations?sort=id,desc" ) )
		                   .andExpect( status().isOk() )
		                   .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
		                   .andExpect( jsonPath( "$.[*].id" ).value( hasItem( location.getId().intValue() ) ) )
		                   .andExpect( jsonPath( "$.[*].latitude" ).value( hasItem( DEFAULT_LATITUDE.doubleValue() ) ) )
		                   .andExpect(
				                   jsonPath( "$.[*].longitude" ).value( hasItem( DEFAULT_LONGITUDE.doubleValue() ) ) )
		                   .andExpect(
				                   jsonPath( "$.[*].description" ).value( hasItem( DEFAULT_DESCRIPTION.toString() ) ) );
	}

	@Test
	@Transactional
	public void getLocation() throws Exception {
		// Initialize the database
		locationRepository.saveAndFlush( location );

		// Get the location
		restLocationMockMvc.perform( get( "/api/locations/{id}", location.getId() ) )
		                   .andExpect( status().isOk() )
		                   .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
		                   .andExpect( jsonPath( "$.id" ).value( location.getId().intValue() ) )
		                   .andExpect( jsonPath( "$.latitude" ).value( DEFAULT_LATITUDE.doubleValue() ) )
		                   .andExpect( jsonPath( "$.longitude" ).value( DEFAULT_LONGITUDE.doubleValue() ) )
		                   .andExpect( jsonPath( "$.description" ).value( DEFAULT_DESCRIPTION.toString() ) );
	}

	@Test
	@Transactional
	public void getNonExistingLocation() throws Exception {
		// Get the location
		restLocationMockMvc.perform( get( "/api/locations/{id}", Long.MAX_VALUE ) )
		                   .andExpect( status().isNotFound() );
	}

	@Test
	@Transactional
	public void updateLocation() throws Exception {
		// Initialize the database
		locationRepository.saveAndFlush( location );
		locationSearchRepository.save( location );
		int databaseSizeBeforeUpdate = locationRepository.findAll().size();

		// Update the location
		Location updatedLocation = locationRepository.findOne( location.getId() );
		updatedLocation
				.latitude( UPDATED_LATITUDE )
				.longitude( UPDATED_LONGITUDE )
				.description( UPDATED_DESCRIPTION );
		LocationDTO locationDTO = locationMapper.toDto( updatedLocation );

		restLocationMockMvc.perform( put( "/api/locations" )
				                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
				                             .content( TestUtil.convertObjectToJsonBytes( locationDTO ) ) )
		                   .andExpect( status().isOk() );

		// Validate the Location in the database
		List<Location> locationList = locationRepository.findAll();
		assertThat( locationList ).hasSize( databaseSizeBeforeUpdate );
		Location testLocation = locationList.get( locationList.size() - 1 );
		assertThat( testLocation.getLatitude() ).isEqualTo( UPDATED_LATITUDE );
		assertThat( testLocation.getLongitude() ).isEqualTo( UPDATED_LONGITUDE );
		assertThat( testLocation.getDescription() ).isEqualTo( UPDATED_DESCRIPTION );

		// Validate the Location in Elasticsearch
		Location locationEs = locationSearchRepository.findOne( testLocation.getId() );
		assertThat( locationEs ).isEqualToComparingFieldByField( testLocation );
	}

	@Test
	@Transactional
	public void updateNonExistingLocation() throws Exception {
		int databaseSizeBeforeUpdate = locationRepository.findAll().size();

		// Create the Location
		LocationDTO locationDTO = locationMapper.toDto( location );

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restLocationMockMvc.perform( put( "/api/locations" )
				                             .contentType( TestUtil.APPLICATION_JSON_UTF8 )
				                             .content( TestUtil.convertObjectToJsonBytes( locationDTO ) ) )
		                   .andExpect( status().isCreated() );

		// Validate the Location in the database
		List<Location> locationList = locationRepository.findAll();
		assertThat( locationList ).hasSize( databaseSizeBeforeUpdate + 1 );
	}

	@Test
	@Transactional
	public void deleteLocation() throws Exception {
		// Initialize the database
		locationRepository.saveAndFlush( location );
		locationSearchRepository.save( location );
		int databaseSizeBeforeDelete = locationRepository.findAll().size();

		// Get the location
		restLocationMockMvc.perform( delete( "/api/locations/{id}", location.getId() )
				                             .accept( TestUtil.APPLICATION_JSON_UTF8 ) )
		                   .andExpect( status().isOk() );

		// Validate Elasticsearch is empty
		boolean locationExistsInEs = locationSearchRepository.exists( location.getId() );
		assertThat( locationExistsInEs ).isFalse();

		// Validate the database is empty
		List<Location> locationList = locationRepository.findAll();
		assertThat( locationList ).hasSize( databaseSizeBeforeDelete - 1 );
	}

	@Test
	@Transactional
	public void searchLocation() throws Exception {
		// Initialize the database
		locationRepository.saveAndFlush( location );
		locationSearchRepository.save( location );

		// Search the location
		restLocationMockMvc.perform( get( "/api/_search/locations?query=id:" + location.getId() ) )
		                   .andExpect( status().isOk() )
		                   .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) )
		                   .andExpect( jsonPath( "$.[*].id" ).value( hasItem( location.getId().intValue() ) ) )
		                   .andExpect( jsonPath( "$.[*].latitude" ).value( hasItem( DEFAULT_LATITUDE.doubleValue() ) ) )
		                   .andExpect(
				                   jsonPath( "$.[*].longitude" ).value( hasItem( DEFAULT_LONGITUDE.doubleValue() ) ) )
		                   .andExpect(
				                   jsonPath( "$.[*].description" ).value( hasItem( DEFAULT_DESCRIPTION.toString() ) ) );
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier( Location.class );
		Location location1 = new Location();
		location1.setId( 1L );
		Location location2 = new Location();
		location2.setId( location1.getId() );
		assertThat( location1 ).isEqualTo( location2 );
		location2.setId( 2L );
		assertThat( location1 ).isNotEqualTo( location2 );
		location1.setId( null );
		assertThat( location1 ).isNotEqualTo( location2 );
	}

	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier( LocationDTO.class );
		LocationDTO locationDTO1 = new LocationDTO();
		locationDTO1.setId( 1L );
		LocationDTO locationDTO2 = new LocationDTO();
		assertThat( locationDTO1 ).isNotEqualTo( locationDTO2 );
		locationDTO2.setId( locationDTO1.getId() );
		assertThat( locationDTO1 ).isEqualTo( locationDTO2 );
		locationDTO2.setId( 2L );
		assertThat( locationDTO1 ).isNotEqualTo( locationDTO2 );
		locationDTO1.setId( null );
		assertThat( locationDTO1 ).isNotEqualTo( locationDTO2 );
	}

	@Test
	@Transactional
	public void testEntityFromId() {
		assertThat( locationMapper.fromId( 42L ).getId() ).isEqualTo( 42 );
		assertThat( locationMapper.fromId( null ) ).isNull();
	}
}
