package be.sandervl.kransenzo.web.rest;

import be.sandervl.kransenzo.domain.Workshop;
import be.sandervl.kransenzo.repository.WorkshopRepository;
import be.sandervl.kransenzo.repository.search.WorkshopSearchRepository;
import be.sandervl.kransenzo.service.dto.WorkshopDTO;
import be.sandervl.kransenzo.service.mapper.WorkshopMapper;
import be.sandervl.kransenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kransenzo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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

    private final WorkshopSearchRepository workshopSearchRepository;

    public WorkshopResource( WorkshopRepository workshopRepository, WorkshopMapper workshopMapper, WorkshopSearchRepository workshopSearchRepository ) {
        this.workshopRepository = workshopRepository;
        this.workshopMapper = workshopMapper;
        this.workshopSearchRepository = workshopSearchRepository;
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
        Workshop workshop = workshopMapper.toEntity( workshopDTO );
        workshop = workshopRepository.save( workshop );
        WorkshopDTO result = workshopMapper.toDto( workshop );
        workshopSearchRepository.save( workshop );
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
            return createWorkshop( workshopDTO );
        }
        Workshop workshop = workshopMapper.toEntity( workshopDTO );
        workshop = workshopRepository.save( workshop );
        WorkshopDTO result = workshopMapper.toDto( workshop );
        workshopSearchRepository.save( workshop );
        return ResponseEntity.ok()
                             .headers( HeaderUtil
                                 .createEntityUpdateAlert( ENTITY_NAME, workshopDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /workshops : get all the workshops.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workshops in body
     */
    @GetMapping("/workshops")
    @Timed
    public List <WorkshopDTO> getAllWorkshops() {
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
    public ResponseEntity <WorkshopDTO> getWorkshop( @PathVariable Long id ) {
        log.debug( "REST request to get Workshop : {}", id );
        Workshop workshop = workshopRepository.findOneWithEagerRelationships( id );
        WorkshopDTO workshopDTO = workshopMapper.toDto( workshop );
        return ResponseUtil.wrapOrNotFound( Optional.ofNullable( workshopDTO ) );
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
        workshopRepository.delete( id );
        workshopSearchRepository.delete( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }

    /**
     * SEARCH  /_search/workshops?query=:query : search for the workshop corresponding
     * to the query.
     *
     * @param query the query of the workshop search
     * @return the result of the search
     */
    @GetMapping("/_search/workshops")
    @Timed
    public List <WorkshopDTO> searchWorkshops( @RequestParam String query ) {
        log.debug( "REST request to search Workshops for query {}", query );
        return StreamSupport
            .stream( workshopSearchRepository.search( queryStringQuery( query ) ).spliterator(), false )
            .map( workshopMapper::toDto )
            .collect( Collectors.toList() );
    }

}
