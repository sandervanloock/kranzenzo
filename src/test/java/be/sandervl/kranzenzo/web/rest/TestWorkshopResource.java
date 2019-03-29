package be.sandervl.kranzenzo.web.rest;

import be.sandervl.kranzenzo.domain.Workshop;
import be.sandervl.kranzenzo.domain.WorkshopDate;
import be.sandervl.kranzenzo.repository.WorkshopRepository;
import be.sandervl.kranzenzo.repository.WorkshopSubscriptionRepository;
import be.sandervl.kranzenzo.service.WorkshopService;
import be.sandervl.kranzenzo.service.dto.WorkshopDTO;
import be.sandervl.kranzenzo.service.mapper.WorkshopMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Sander Van Loock
 */
@RunWith(MockitoJUnitRunner.class)
public class TestWorkshopResource {

    public static final ZonedDateTime NOW = ZonedDateTime.now();
    private WorkshopResource workshopResource;
    @Mock
    private WorkshopRepository workshopRepository;
    @Mock
    private WorkshopMapper mapper;
    @Mock
    private WorkshopSubscriptionRepository subscriptionRepo;
    @Mock
    private WorkshopService workshopService;

    @Before
    public void setUp() throws Exception {
        workshopResource = new WorkshopResource( workshopRepository, mapper, subscriptionRepo, workshopService );
    }

    @Test
    public void getHomepageWorkshop() {
        Workshop ws1 = new Workshop().addDates( new WorkshopDate().date( NOW.minusDays( 5 ) ) )
                                     .addDates( new WorkshopDate().date( NOW.minusDays( 10 ) ) );
        Workshop ws2 = new Workshop().addDates( new WorkshopDate().date( NOW.plusDays( 2 ) ) )
                                     .addDates( new WorkshopDate().date( NOW.plusDays( 3 ) ) );
        Workshop ws3 = new Workshop().addDates( new WorkshopDate().date( NOW.plusMinutes( 1 ) ) );
        Workshop ws4 = new Workshop();
        ws1.setId( 1L );
        ws2.setId( 2L );
        ws3.setId( 3L );
        ws4.setId( 4L );
        when( workshopRepository.findByShowOnHomepageAndIsActive() ).thenReturn( Arrays.asList(
            ws1,
            ws2,
            ws3,
            ws4
        ) );
        when( mapper.toDto( any( Workshop.class ) ) ).thenAnswer( ( args ) -> {
            WorkshopDTO dto = new WorkshopDTO();
            dto.setId( ((Workshop) args.getArgument( 0 )).getId() );
            return dto;
        } );
        ResponseEntity <List <WorkshopDTO>> response = workshopResource.getHomepageWorkshops();

        assertEquals( ws3.getId(), response.getBody().get( 0 ).getId() );
    }
}
