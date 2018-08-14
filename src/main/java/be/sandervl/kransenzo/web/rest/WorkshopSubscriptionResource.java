package be.sandervl.kransenzo.web.rest;

import be.sandervl.kransenzo.domain.User;
import be.sandervl.kransenzo.domain.WorkshopSubscription;
import be.sandervl.kransenzo.repository.WorkshopSubscriptionRepository;
import be.sandervl.kransenzo.repository.search.WorkshopSubscriptionSearchRepository;
import be.sandervl.kransenzo.service.MailService;
import be.sandervl.kransenzo.service.UserService;
import be.sandervl.kransenzo.service.dto.WorkshopSubscriptionDTO;
import be.sandervl.kransenzo.service.mapper.WorkshopSubscriptionMapper;
import be.sandervl.kransenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kransenzo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing WorkshopSubscription.
 */
@RestController
@RequestMapping("/api")
public class WorkshopSubscriptionResource {

    private static final String ENTITY_NAME = "workshopSubscription";
    private final Logger log = LoggerFactory.getLogger( WorkshopSubscriptionResource.class );
    private final WorkshopSubscriptionRepository workshopSubscriptionRepository;

    private final WorkshopSubscriptionMapper workshopSubscriptionMapper;

    private final WorkshopSubscriptionSearchRepository workshopSubscriptionSearchRepository;
    private final UserService userService;
    private final MailService mailService;

    public WorkshopSubscriptionResource( WorkshopSubscriptionRepository workshopSubscriptionRepository, WorkshopSubscriptionMapper workshopSubscriptionMapper, WorkshopSubscriptionSearchRepository workshopSubscriptionSearchRepository, UserService userService, MailService mailService ) {
        this.workshopSubscriptionRepository = workshopSubscriptionRepository;
        this.workshopSubscriptionMapper = workshopSubscriptionMapper;
        this.workshopSubscriptionSearchRepository = workshopSubscriptionSearchRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * POST  /workshop-subscriptions : Create a new workshopSubscription.
     *
     * @param workshopSubscriptionDTO the workshopSubscriptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workshopSubscriptionDTO, or with status 400 (Bad Request) if the workshopSubscription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/workshop-subscriptions")
    @Timed
    public ResponseEntity <WorkshopSubscriptionDTO> createWorkshopSubscription( @RequestBody WorkshopSubscriptionDTO workshopSubscriptionDTO ) throws URISyntaxException {
        log.debug( "REST request to save WorkshopSubscription : {}", workshopSubscriptionDTO );
        if ( workshopSubscriptionDTO.getId() != null ) {
            throw new BadRequestAlertException( "A new workshopSubscription cannot already have an ID", ENTITY_NAME, "idexists" );
        }
        WorkshopSubscription workshopSubscription = workshopSubscriptionMapper.toEntity( workshopSubscriptionDTO );
        if ( workshopSubscriptionDTO.getUser() != null ) {
            //fetch customer eager so all information for sending the email is present on the order
            User user = userService.findUserByEmail( workshopSubscriptionDTO.getUser().getEmail() )
                                   .orElseGet( () -> userService.createUser( workshopSubscriptionDTO.getUser() ) );
            workshopSubscription.setUser( user );
            mailService.sendWorkshopSubscriptionMails( workshopSubscription );
        }
        workshopSubscription = workshopSubscriptionRepository.save( workshopSubscription );
        WorkshopSubscriptionDTO result = workshopSubscriptionMapper.toDto( workshopSubscription );
        //workshopSubscriptionSearchRepository.save( workshopSubscription );
        return ResponseEntity.created( new URI( "/api/workshop-subscriptions/" + result.getId() ) )
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( result );
    }

    /**
     * PUT  /workshop-subscriptions : Updates an existing workshopSubscription.
     *
     * @param workshopSubscriptionDTO the workshopSubscriptionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workshopSubscriptionDTO,
     * or with status 400 (Bad Request) if the workshopSubscriptionDTO is not valid,
     * or with status 500 (Internal Server Error) if the workshopSubscriptionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/workshop-subscriptions")
    @Timed
    public ResponseEntity <WorkshopSubscriptionDTO> updateWorkshopSubscription( @RequestBody WorkshopSubscriptionDTO workshopSubscriptionDTO ) throws URISyntaxException {
        log.debug( "REST request to update WorkshopSubscription : {}", workshopSubscriptionDTO );
        if ( workshopSubscriptionDTO.getId() == null ) {
            return createWorkshopSubscription( workshopSubscriptionDTO );
        }
        WorkshopSubscription workshopSubscription = workshopSubscriptionMapper.toEntity( workshopSubscriptionDTO );
        workshopSubscription = workshopSubscriptionRepository.save( workshopSubscription );
        WorkshopSubscriptionDTO result = workshopSubscriptionMapper.toDto( workshopSubscription );
        //workshopSubscriptionSearchRepository.save( workshopSubscription );
        return ResponseEntity.ok()
                             .headers( HeaderUtil
                                 .createEntityUpdateAlert( ENTITY_NAME, workshopSubscriptionDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /workshop-subscriptions : get all the workshopSubscriptions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workshopSubscriptions in body
     */
    @GetMapping("/workshop-subscriptions")
    @Timed
    public List <WorkshopSubscriptionDTO> getAllWorkshopSubscriptions() {
        log.debug( "REST request to get all WorkshopSubscriptions" );
        List <WorkshopSubscription> workshopSubscriptions = workshopSubscriptionRepository.findAll();
        return workshopSubscriptionMapper.toDto( workshopSubscriptions );
    }

    /**
     * GET  /workshop-subscriptions/:id : get the "id" workshopSubscription.
     *
     * @param id the id of the workshopSubscriptionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workshopSubscriptionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/workshop-subscriptions/{id}")
    @Timed
    public ResponseEntity <WorkshopSubscriptionDTO> getWorkshopSubscription( @PathVariable Long id ) {
        log.debug( "REST request to get WorkshopSubscription : {}", id );
        WorkshopSubscription workshopSubscription = workshopSubscriptionRepository.findOne( id );
        WorkshopSubscriptionDTO workshopSubscriptionDTO = workshopSubscriptionMapper.toDto( workshopSubscription );
        return ResponseUtil.wrapOrNotFound( Optional.ofNullable( workshopSubscriptionDTO ) );
    }

    /**
     * DELETE  /workshop-subscriptions/:id : delete the "id" workshopSubscription.
     *
     * @param id the id of the workshopSubscriptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/workshop-subscriptions/{id}")
    @Timed
    public ResponseEntity <Void> deleteWorkshopSubscription( @PathVariable Long id ) {
        log.debug( "REST request to delete WorkshopSubscription : {}", id );
        workshopSubscriptionRepository.delete( id );
        //workshopSubscriptionSearchRepository.delete( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }

    /**
     * SEARCH  /_search/workshop-subscriptions?query=:query : search for the workshopSubscription corresponding
     * to the query.
     *
     * @param query the query of the workshopSubscription search
     * @return the result of the search
     */
    @GetMapping("/_search/workshop-subscriptions")
    @Timed
    public List <WorkshopSubscriptionDTO> searchWorkshopSubscriptions( @RequestParam String query ) {
        log.debug( "REST request to search WorkshopSubscriptions for query {}", query );
        return StreamSupport
            .stream( workshopSubscriptionSearchRepository.search( queryStringQuery( query ) ).spliterator(), false )
            .map( workshopSubscriptionMapper::toDto )
            .collect( Collectors.toList() );
    }

}
