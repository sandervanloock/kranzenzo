package be.sandervl.kranzenzo.service;

import be.sandervl.kranzenzo.domain.Workshop;
import be.sandervl.kranzenzo.repository.ImageRepository;
import be.sandervl.kranzenzo.repository.WorkshopDateRepository;
import be.sandervl.kranzenzo.repository.WorkshopRepository;
import be.sandervl.kranzenzo.service.dto.WorkshopDTO;
import be.sandervl.kranzenzo.service.mapper.WorkshopMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Sander Van Loock
 */
@Service
@Transactional
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;
    private final ImageRepository imageRepository;
    private final WorkshopDateRepository workshopDateRepository;

    public WorkshopService( WorkshopRepository workshopRepository, WorkshopMapper workshopMapper, ImageRepository imageRepository, WorkshopDateRepository workshopDateRepository ) {
        this.workshopRepository = workshopRepository;
        this.workshopMapper = workshopMapper;
        this.imageRepository = imageRepository;
        this.workshopDateRepository = workshopDateRepository;
    }

    public WorkshopDTO save( WorkshopDTO workshopDTO ) {
        final Workshop workshop = workshopMapper.toEntity( workshopDTO );
        Optional<Workshop> optionalWorkshop = workshopRepository.findOneWithEagerRelationships( workshopDTO.getId() );
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
        return workshopMapper.toDto( workshop );
    }
}
