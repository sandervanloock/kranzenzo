package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.domain.HomepageSettings;
import be.sandervl.kranzenzo.repository.HomepageSettingsRepository;
import be.sandervl.kranzenzo.service.dto.HomepageSettingsDTO;
import be.sandervl.kranzenzo.service.mapper.HomepageSettingsMapper;
import be.sandervl.kranzenzo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class HompepageSettingsResource {

    private static final String ENTITY_NAME = "homepage-settings";
    private final Logger log = LoggerFactory.getLogger( HompepageSettingsResource.class );
    private final HomepageSettingsRepository repository;
    private HomepageSettingsMapper settingsMapper;

    public HompepageSettingsResource( HomepageSettingsRepository repository, HomepageSettingsMapper settingsMapper ) {
        this.repository = repository;
        this.settingsMapper = settingsMapper;
    }

    @PutMapping("/homepage-settings")
    @Timed
    public ResponseEntity <HomepageSettingsDTO> createHomepage( @Valid @RequestBody HomepageSettingsDTO settingsDto ) throws URISyntaxException {
        log.debug( "REST request to save Product : {}", settingsDto );
        HomepageSettings homepageSettings = settingsMapper.toEntity( settingsDto );
        //only one entry is allowed in the table
        repository.deleteAll();
        HomepageSettings result = repository.save( homepageSettings );
        return ResponseEntity.ok()
                             .headers( HeaderUtil.createEntityCreationAlert( ENTITY_NAME, result.getId().toString() ) )
                             .body( settingsMapper.toDto( result ) );
    }

    @GetMapping("/homepage-settings")
    @Timed
    public ResponseEntity <HomepageSettingsDTO> getHomepage() {
        log.debug( "REST request to get all Products" );
        List <HomepageSettings> all = repository.findAll();
        if ( !all.isEmpty() ) {
            return ResponseEntity.ok( settingsMapper.toDto( all.get( 0 ) ) );
        }
        return ResponseEntity.notFound().build();
    }

}
