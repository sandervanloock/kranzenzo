package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.domain.Workshop;
import be.sandervl.kranzenzo.domain.enumeration.SubscriptionState;
import be.sandervl.kranzenzo.repository.ImageRepository;
import be.sandervl.kranzenzo.repository.WorkshopDateRepository;
import be.sandervl.kranzenzo.repository.WorkshopRepository;
import be.sandervl.kranzenzo.repository.WorkshopSubscriptionRepository;
import be.sandervl.kranzenzo.service.dto.WorkshopDTO;
import be.sandervl.kranzenzo.service.mapper.WorkshopMapper;
import be.sandervl.kranzenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kranzenzo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Workshop.
 */
@RestController
@RequestMapping("/api")
public class WorkshopResource {

    private static final String ENTITY_NAME = "workshop";
    private final Logger log = LoggerFactory.getLogger( WorkshopResource.class );
    private final WorkshopRepository workshopRepository;

    private final WorkshopMapper workshopMapper;
    private final ImageRepository imageRepository;
    private final WorkshopDateRepository workshopDateRepository;
    private final WorkshopSubscriptionRepository workshopSubscriptionRepository;

    public WorkshopResource( WorkshopRepository workshopRepository, WorkshopMapper workshopMapper, ImageRepository imageRepository, WorkshopDateRepository workshopDateRepository, WorkshopSubscriptionRepository workshopSubscriptionRepository ) {
        this.workshopRepository = workshopRepository;
        this.workshopMapper = workshopMapper;
        this.imageRepository = imageRepository;
        this.workshopDateRepository = workshopDateRepository;
        this.workshopSubscriptionRepository = workshopSubscriptionRepository;
    }

    /**
     * POST  /workshops : Create a new workshop.
     *
     * @param workshopDTO the workshopDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workshopDTO, or with status 400 (Bad Request) if the workshop has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/workshops")
    @Timed
    public ResponseEntity <WorkshopDTO> createWorkshop( @Valid @RequestBody WorkshopDTO workshopDTO ) throws URISyntaxException {
        log.debug( "REST request to save Workshop : {}", workshopDTO );
        if ( workshopDTO.getId() != null ) {
            throw new BadRequestAlertException( "A new workshop cannot already have an ID", ENTITY_NAME, "idexists" );
        }

        Workshop workshop = handleImages( workshopDTO );

        WorkshopDTO result = workshopMapper.toDto( workshop );
        return ResponseEntity.created( new URI( "/api/workshops/" + result.getId() ) )
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( result );
    }

    /**
     * PUT  /workshops : Updates an existing workshop.
     *
     * @param workshopDTO the workshopDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workshopDTO,
     * or with status 400 (Bad Request) if the workshopDTO is not valid,
     * or with status 500 (Internal Server Error) if the workshopDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/workshops")
    @Timed
    public ResponseEntity <WorkshopDTO> updateWorkshop( @Valid @RequestBody WorkshopDTO workshopDTO ) throws URISyntaxException {
        log.debug( "REST request to update Workshop : {}", workshopDTO );
        if ( workshopDTO.getId() == null ) {
            throw new BadRequestAlertException( "Invalid id", ENTITY_NAME, "idnull" );
        }

        Workshop workshop = handleImages( workshopDTO );

        WorkshopDTO result = workshopMapper.toDto( workshop );
        return ResponseEntity.ok()
                             .headers( HeaderUtil
                                 .createEntityUpdateAlert( ENTITY_NAME, workshopDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /workshops : get all the workshops.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of workshops in body
     */
    @GetMapping("/workshops")
    @Timed
    public List <WorkshopDTO> getAllWorkshops( @RequestParam(required = false, defaultValue = "false") boolean eagerload ) {
        log.debug( "REST request to get all Workshops" );
        List <Workshop> workshops = workshopRepository.findAllWithEagerRelationships();
        return workshopMapper.toDto( workshops );
    }

    /**
     * GET  /workshops/:id : get the "id" workshop.
     *
     * @param id the id of the workshopDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workshopDTO, or with status 404 (Not Found)
     */
    @GetMapping("/workshops/{id}")
    @Timed
    public ResponseEntity <WorkshopDTO> getWorkshop( @PathVariable Long id, @RequestParam(value = "filterDates", required = false) Boolean filterDates ) {
        log.debug( "REST request to get Workshop : {}", id );
        Optional <WorkshopDTO> workshopDTO =
            workshopRepository.findOneWithEagerRelationships( id )
                              .map( workshop -> {
                                  if ( workshop.getDates() != null && !workshop.getDates().isEmpty() && filterDates ) {
                                      log.debug( "Filter out past dates and dates with max subscriptions" );
                                      workshop.setDates( workshop.getDates()
                                                                 .stream()
                                                                 .filter( date -> date.getDate().isAfter( ZonedDateTime
                                                                     .now() ) )
                                                                 .filter( date -> workshopSubscriptionRepository
                                                                     .countByWorkshopAndState( date, SubscriptionState.PAYED ) < workshop
                                                                     .getMaxSubscriptions() )
                                                                 .collect( Collectors.toSet() ) );
                                  }
                                  return workshop;
                              } )
                              .map( workshopMapper::toDto );
        return ResponseUtil.wrapOrNotFound( workshopDTO );
    }

    /**
     * DELETE  /workshops/:id : delete the "id" workshop.
     *
     * @param id the id of the workshopDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/workshops/{id}")
    @Timed
    public ResponseEntity <Void> deleteWorkshop( @PathVariable Long id ) {
        log.debug( "REST request to delete Workshop : {}", id );

        workshopRepository.deleteById( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }

    private Workshop handleImages( WorkshopDTO workshopDTO ) {
        final Workshop workshop = workshopMapper.toEntity( workshopDTO );
        Optional <Workshop> optionalWorkshop = workshopRepository.findOneWithEagerRelationships( workshopDTO.getId() );
        //unlink existing images
        optionalWorkshop.ifPresent( existing -> existing
            .getImages()
            .stream()
            .filter( image -> !workshop.getImages().contains( image ) )
            .peek( image -> image.setWorkshop( null ) )
            .forEach( imageRepository::save ) );

        //link new images
        workshopRepository.save( workshop )
                          .getImages()
                          .stream()
                          .filter( image -> image.getId() != null )
                          //get image from database
                          .peek( image -> imageRepository.findById( image.getId() )
                                                         .ifPresent( i -> i.setWorkshop( workshop ) ) )
                          .forEach( imageRepository::save );
        //save new dates
        workshop.getDates()
                .stream()
                .peek( date -> date.setWorkshop( workshop ) )
                .forEach( workshopDateRepository::save );
        //delete removed dates
        optionalWorkshop.ifPresent( existing -> existing
            .getDates()
            .stream()
            .filter( date -> !workshop.getDates().contains( date ) )
            .forEach( workshopDateRepository::delete ) );
        return workshop;
    }
}
