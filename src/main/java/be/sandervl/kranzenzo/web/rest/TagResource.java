package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.domain.Tag;
import be.sandervl.kranzenzo.repository.TagRepository;
import be.sandervl.kranzenzo.service.dto.TagDTO;
import be.sandervl.kranzenzo.service.mapper.TagMapper;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tag.
 */
@RestController
@RequestMapping("/api")
public class TagResource {

    private static final String ENTITY_NAME = "tag";
    private final Logger log = LoggerFactory.getLogger( TagResource.class );
    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    public TagResource( TagRepository tagRepository, TagMapper tagMapper ) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    /**
     * POST  /tags : Create a new tag.
     *
     * @param tagDTO the tagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tagDTO, or with status 400 (Bad Request) if the tag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tags")
    @Timed
    public ResponseEntity <TagDTO> createTag( @Valid @RequestBody TagDTO tagDTO ) throws URISyntaxException {
        log.debug( "REST request to save Tag : {}", tagDTO );
        if ( tagDTO.getId() != null ) {
            throw new BadRequestAlertException( "A new tag cannot already have an ID", ENTITY_NAME, "idexists" );
        }

        Tag tag = tagMapper.toEntity( tagDTO );
        tag = tagRepository.save( tag );
        TagDTO result = tagMapper.toDto( tag );
        return ResponseEntity.created( new URI( "/api/tags/" + result.getId() ) )
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( result );
    }

    /**
     * PUT  /tags : Updates an existing tag.
     *
     * @param tagDTO the tagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tagDTO,
     * or with status 400 (Bad Request) if the tagDTO is not valid,
     * or with status 500 (Internal Server Error) if the tagDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tags")
    @Timed
    public ResponseEntity <TagDTO> updateTag( @Valid @RequestBody TagDTO tagDTO ) throws URISyntaxException {
        log.debug( "REST request to update Tag : {}", tagDTO );
        if ( tagDTO.getId() == null ) {
            throw new BadRequestAlertException( "Invalid id", ENTITY_NAME, "idnull" );
        }

        Tag tag = tagMapper.toEntity( tagDTO );
        tag = tagRepository.save( tag );
        TagDTO result = tagMapper.toDto( tag );
        return ResponseEntity.ok()
                             .headers( HeaderUtil.createEntityUpdateAlert( ENTITY_NAME, tagDTO.getId().toString() ) )
                             .body( result );
    }

    /**
     * GET  /tags : get all the tags.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tags in body
     */
    @GetMapping("/tags")
    @Timed
    public List <TagDTO> getAllTags() {
        log.debug( "REST request to get all Tags" );
        List <Tag> tags = tagRepository.findAll();
        return tagMapper.toDto( tags );
    }

    /**
     * GET  /tags/:id : get the "id" tag.
     *
     * @param id the id of the tagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tagDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tags/{id}")
    @Timed
    public ResponseEntity <TagDTO> getTag( @PathVariable Long id ) {
        log.debug( "REST request to get Tag : {}", id );
        Optional <TagDTO> tagDTO = tagRepository.findById( id )
                                                .map( tagMapper::toDto );
        return ResponseUtil.wrapOrNotFound( tagDTO );
    }

    /**
     * DELETE  /tags/:id : delete the "id" tag.
     *
     * @param id the id of the tagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tags/{id}")
    @Timed
    public ResponseEntity <Void> deleteTag( @PathVariable Long id ) {
        log.debug( "REST request to delete Tag : {}", id );

        tagRepository.deleteById( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString() ) )
                             .build();
    }
}
