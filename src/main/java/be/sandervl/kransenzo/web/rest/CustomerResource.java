package be.sandervl.kransenzo.web.rest;

import be.sandervl.kransenzo.domain.Customer;
import be.sandervl.kransenzo.domain.Location;
import be.sandervl.kransenzo.domain.User;
import be.sandervl.kransenzo.repository.CustomerRepository;
import be.sandervl.kransenzo.repository.LocationRepository;
import be.sandervl.kransenzo.repository.UserRepository;
import be.sandervl.kransenzo.repository.search.CustomerSearchRepository;
import be.sandervl.kransenzo.service.dto.CustomerDTO;
import be.sandervl.kransenzo.service.mapper.CustomerMapper;
import be.sandervl.kransenzo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource
{

    private static final String ENTITY_NAME = "customer";
    private final Logger log = LoggerFactory.getLogger( CustomerResource.class );
    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final CustomerSearchRepository customerSearchRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public CustomerResource(CustomerRepository customerRepository,
                            CustomerMapper customerMapper,
                            CustomerSearchRepository customerSearchRepository,
                            PasswordEncoder passwordEncoder,
                            UserRepository userRepository,
                            LocationRepository locationRepository){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.customerSearchRepository = customerSearchRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * POST  /customers : Create a new customer.
     *
     * @param customerDTO the customerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerDTO, or with status 400 (Bad Request) if the customer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customers")
    @Timed
    public ResponseEntity<CustomerDTO> createCustomer( @Valid @RequestBody CustomerDTO customerDTO ) throws URISyntaxException {
        log.debug( "REST request to save Customer : {}", customerDTO );
        if ( customerDTO.getId() != null ) {
            return ResponseEntity.badRequest().headers( HeaderUtil.createFailureAlert( ENTITY_NAME, "idexists",
                                                                                       "A new customer cannot already have an ID" ) )
                                 .body( null );
        }
        Customer customer = customerMapper.toEntity( customerDTO );
        setUserLocation(customer);
        setUserLoginAndPassword( customer );
        customer = customerRepository.save( customer );
        CustomerDTO result = customerMapper.toDto( customer );
//        customerSearchRepository.save( customer );
        return ResponseEntity.created( new URI( "/api/customers/" + result.getId() ) )
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( result );
    }

    private void setUserLocation(Customer customer){
        Location address = customer.getAddress();
        if (Objects.nonNull(address.getLatitude()) && Objects.nonNull(address.getLongitude())){
            Location location = locationRepository.save(address);
            customer.setAddress(location);
        }
        else{
            customer.setAddress(null);
        }
    }

    private void setUserLoginAndPassword( Customer customer ) {
        User user = customer.getUser();
        if ( user != null ) {
            if ( user.getId() != null ) {
                Optional.ofNullable( userRepository.findOne( user.getId() ) )
                        .ifPresent( keepOriginalLoginAndPassword( user ) );
            }
            if ( StringUtils.isNotBlank( user.getEmail() ) ) {
                userRepository.findOneByEmailIgnoreCase( user.getEmail() )
                              .ifPresent( keepOriginalLoginAndPassword( user ) );
            }
            if ( StringUtils.isAnyBlank( user.getLogin(), user.getPassword() ) ) {
                user.setLogin( UUID.randomUUID().toString() );
                user.setPassword( passwordEncoder.encode( "NO_PASS" ) );
                customer.setUser( userRepository.save( user ) );
            }
        }
    }

    private Consumer <User> keepOriginalLoginAndPassword( User user ) {
        return existing -> {
            user.setId( existing.getId() );
            user.setLogin( existing.getLogin() );
            user.setPassword( existing.getPassword() );
        };
    }

    /**
     * PUT  /customers : Updates an existing customer.
     *
     * @param customerDTO the customerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerDTO,
     * or with status 400 (Bad Request) if the customerDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customers")
    @Timed
    public ResponseEntity<CustomerDTO> updateCustomer( @Valid @RequestBody CustomerDTO customerDTO ) throws URISyntaxException {
        log.debug( "REST request to update Customer : {}", customerDTO );
        if ( customerDTO.getId() == null ) {
            return createCustomer( customerDTO );
        }
        Customer customer = customerMapper.toEntity( customerDTO );
        setUserLoginAndPassword( customer );
        customer = customerRepository.save( customer );
        CustomerDTO result = customerMapper.toDto( customer );
//        customerSearchRepository.save( customer );
        return ResponseEntity.ok()
                             .headers(
                                     HeaderUtil.createEntityUpdateAlert( ENTITY_NAME, customerDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /customers : get all the customers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     */
    @GetMapping("/customers")
    @Timed
    public List<CustomerDTO> getAllCustomers() {
        log.debug( "REST request to get all Customers" );
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDto( customers );
    }

    /**
     * GET  /customers/:id : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customers/{id}")
    @Timed
    public ResponseEntity<CustomerDTO> getCustomer( @PathVariable Long id ) {
        log.debug( "REST request to get Customer : {}", id );
        Customer customer = customerRepository.findOne( id );
        CustomerDTO customerDTO = customerMapper.toDto( customer );
        return ResponseUtil.wrapOrNotFound( Optional.ofNullable( customerDTO ) );
    }

    /**
     * DELETE  /customers/:id : delete the "id" customer.
     *
     * @param id the id of the customerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomer( @PathVariable Long id ) {
        log.debug( "REST request to delete Customer : {}", id );
        customerRepository.delete( id );
//        customerSearchRepository.delete( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }

    /**
     * SEARCH  /_search/customers?query=:query : search for the customer corresponding
     * to the query.
     *
     * @param query the query of the customer search
     * @return the result of the search
     */
    @GetMapping("/_search/customers")
    @Timed
    public List<CustomerDTO> searchCustomers( @RequestParam String query ) {
        log.debug( "REST request to search Customers for query {}", query );
        return StreamSupport
                .stream( customerSearchRepository.search( queryStringQuery( query ) ).spliterator(), false )
                .map( customerMapper::toDto )
                .collect( Collectors.toList() );
    }

}
