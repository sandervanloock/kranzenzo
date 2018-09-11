package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.domain.WorkshopSubscription;
import be.sandervl.kranzenzo.repository.WorkshopSubscriptionRepository;
import be.sandervl.kranzenzo.service.dto.WorkshopSubscriptionDTO;
import be.sandervl.kranzenzo.service.mapper.WorkshopSubscriptionMapper;
import be.sandervl.kranzenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kranzenzo.web.rest.util.HeaderUtil;
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

    public WorkshopSubscriptionResource( WorkshopSubscriptionRepository workshopSubscriptionRepository, WorkshopSubscriptionMapper workshopSubscriptionMapper ) {
        this.workshopSubscriptionRepository = workshopSubscriptionRepository;
        this.workshopSubscriptionMapper = workshopSubscriptionMapper;
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
        workshopSubscription = workshopSubscriptionRepository.save( workshopSubscription );
        WorkshopSubscriptionDTO result = workshopSubscriptionMapper.toDto( workshopSubscription );
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
            throw new BadRequestAlertException( "Invalid id", ENTITY_NAME, "idnull" );
        }

        WorkshopSubscription workshopSubscription = workshopSubscriptionMapper.toEntity( workshopSubscriptionDTO );
        workshopSubscription = workshopSubscriptionRepository.save( workshopSubscription );
        WorkshopSubscriptionDTO result = workshopSubscriptionMapper.toDto( workshopSubscription );
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
        Optional <WorkshopSubscriptionDTO> workshopSubscriptionDTO = workshopSubscriptionRepository.findById( id )
                                                                                                   .map( workshopSubscriptionMapper::toDto );
        return ResponseUtil.wrapOrNotFound( workshopSubscriptionDTO );
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

        workshopSubscriptionRepository.deleteById( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }
}
