package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.domain.WorkshopSubscription;
import be.sandervl.kranzenzo.repository.WorkshopSubscriptionRepository;
import be.sandervl.kranzenzo.web.rest.errors.BadRequestAlertException;
import be.sandervl.kranzenzo.service.dto.WorkshopSubscriptionDTO;
import be.sandervl.kranzenzo.service.mapper.WorkshopSubscriptionMapper;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link be.sandervl.kranzenzo.domain.WorkshopSubscription}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkshopSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(WorkshopSubscriptionResource.class);

    private static final String ENTITY_NAME = "workshopSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkshopSubscriptionRepository workshopSubscriptionRepository;

    private final WorkshopSubscriptionMapper workshopSubscriptionMapper;

    public WorkshopSubscriptionResource(WorkshopSubscriptionRepository workshopSubscriptionRepository, WorkshopSubscriptionMapper workshopSubscriptionMapper) {
        this.workshopSubscriptionRepository = workshopSubscriptionRepository;
        this.workshopSubscriptionMapper = workshopSubscriptionMapper;
    }

    /**
     * {@code POST  /workshop-subscriptions} : Create a new workshopSubscription.
     *
     * @param workshopSubscriptionDTO the workshopSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workshopSubscriptionDTO, or with status {@code 400 (Bad Request)} if the workshopSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workshop-subscriptions")
    public ResponseEntity<WorkshopSubscriptionDTO> createWorkshopSubscription(@RequestBody WorkshopSubscriptionDTO workshopSubscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save WorkshopSubscription : {}", workshopSubscriptionDTO);
        if (workshopSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new workshopSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkshopSubscription workshopSubscription = workshopSubscriptionMapper.toEntity(workshopSubscriptionDTO);
        workshopSubscription = workshopSubscriptionRepository.save(workshopSubscription);
        WorkshopSubscriptionDTO result = workshopSubscriptionMapper.toDto(workshopSubscription);
        return ResponseEntity.created(new URI("/api/workshop-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workshop-subscriptions} : Updates an existing workshopSubscription.
     *
     * @param workshopSubscriptionDTO the workshopSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workshopSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the workshopSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workshopSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workshop-subscriptions")
    public ResponseEntity<WorkshopSubscriptionDTO> updateWorkshopSubscription(@RequestBody WorkshopSubscriptionDTO workshopSubscriptionDTO) throws URISyntaxException {
        log.debug("REST request to update WorkshopSubscription : {}", workshopSubscriptionDTO);
        if (workshopSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkshopSubscription workshopSubscription = workshopSubscriptionMapper.toEntity(workshopSubscriptionDTO);
        workshopSubscription = workshopSubscriptionRepository.save(workshopSubscription);
        WorkshopSubscriptionDTO result = workshopSubscriptionMapper.toDto(workshopSubscription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workshopSubscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /workshop-subscriptions} : get all the workshopSubscriptions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workshopSubscriptions in body.
     */
    @GetMapping("/workshop-subscriptions")
    public List<WorkshopSubscriptionDTO> getAllWorkshopSubscriptions() {
        log.debug("REST request to get all WorkshopSubscriptions");
        List<WorkshopSubscription> workshopSubscriptions = workshopSubscriptionRepository.findAll();
        return workshopSubscriptionMapper.toDto(workshopSubscriptions);
    }

    /**
     * {@code GET  /workshop-subscriptions/:id} : get the "id" workshopSubscription.
     *
     * @param id the id of the workshopSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workshopSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workshop-subscriptions/{id}")
    public ResponseEntity<WorkshopSubscriptionDTO> getWorkshopSubscription(@PathVariable Long id) {
        log.debug("REST request to get WorkshopSubscription : {}", id);
        Optional<WorkshopSubscriptionDTO> workshopSubscriptionDTO = workshopSubscriptionRepository.findById(id)
            .map(workshopSubscriptionMapper::toDto);
        return ResponseUtil.wrapOrNotFound(workshopSubscriptionDTO);
    }

    /**
     * {@code DELETE  /workshop-subscriptions/:id} : delete the "id" workshopSubscription.
     *
     * @param id the id of the workshopSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workshop-subscriptions/{id}")
    public ResponseEntity<Void> deleteWorkshopSubscription(@PathVariable Long id) {
        log.debug("REST request to delete WorkshopSubscription : {}", id);
        workshopSubscriptionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
