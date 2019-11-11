package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.domain.Workshop;
import be.sandervl.kranzenzo.repository.WorkshopRepository;
import be.sandervl.kranzenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kranzenzo.service.dto.WorkshopDTO;
import be.sandervl.kranzenzo.service.mapper.WorkshopMapper;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link be.sandervl.kranzenzo.domain.Workshop}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkshopResource {

    private final Logger log = LoggerFactory.getLogger(WorkshopResource.class);

    private static final String ENTITY_NAME = "workshop";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkshopRepository workshopRepository;

    private final WorkshopMapper workshopMapper;

    public WorkshopResource(WorkshopRepository workshopRepository, WorkshopMapper workshopMapper) {
        this.workshopRepository = workshopRepository;
        this.workshopMapper = workshopMapper;
    }

    /**
     * {@code POST  /workshops} : Create a new workshop.
     *
     * @param workshopDTO the workshopDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workshopDTO, or with status {@code 400 (Bad Request)} if the workshop has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workshops")
    public ResponseEntity<WorkshopDTO> createWorkshop(@Valid @RequestBody WorkshopDTO workshopDTO) throws URISyntaxException {
        log.debug("REST request to save Workshop : {}", workshopDTO);
        if (workshopDTO.getId() != null) {
            throw new BadRequestAlertException("A new workshop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Workshop workshop = workshopMapper.toEntity(workshopDTO);
        workshop = workshopRepository.save(workshop);
        WorkshopDTO result = workshopMapper.toDto(workshop);
        return ResponseEntity.created(new URI("/api/workshops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workshops} : Updates an existing workshop.
     *
     * @param workshopDTO the workshopDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workshopDTO,
     * or with status {@code 400 (Bad Request)} if the workshopDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workshopDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workshops")
    public ResponseEntity<WorkshopDTO> updateWorkshop(@Valid @RequestBody WorkshopDTO workshopDTO) throws URISyntaxException {
        log.debug("REST request to update Workshop : {}", workshopDTO);
        if (workshopDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Workshop workshop = workshopMapper.toEntity(workshopDTO);
        workshop = workshopRepository.save(workshop);
        WorkshopDTO result = workshopMapper.toDto(workshop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workshopDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /workshops} : get all the workshops.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workshops in body.
     */
    @GetMapping("/workshops")
    public List<WorkshopDTO> getAllWorkshops(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Workshops");
        List<Workshop> workshops = workshopRepository.findAllWithEagerRelationships();
        return workshopMapper.toDto(workshops);
    }

    /**
     * {@code GET  /workshops/:id} : get the "id" workshop.
     *
     * @param id the id of the workshopDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workshopDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workshops/{id}")
    public ResponseEntity<WorkshopDTO> getWorkshop(@PathVariable Long id) {
        log.debug("REST request to get Workshop : {}", id);
        Optional<WorkshopDTO> workshopDTO = workshopRepository.findOneWithEagerRelationships(id)
            .map(workshopMapper::toDto);
        return ResponseUtil.wrapOrNotFound(workshopDTO);
    }

    /**
     * {@code DELETE  /workshops/:id} : delete the "id" workshop.
     *
     * @param id the id of the workshopDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workshops/{id}")
    public ResponseEntity<Void> deleteWorkshop(@PathVariable Long id) {
        log.debug("REST request to delete Workshop : {}", id);
        workshopRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
