package be.sandervl.kranzenzo.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import be.sandervl.kranzenzo.web.rest.TestUtil;

public class WorkshopDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkshopDTO.class);
        WorkshopDTO workshopDTO1 = new WorkshopDTO();
        workshopDTO1.setId(1L);
        WorkshopDTO workshopDTO2 = new WorkshopDTO();
        assertThat(workshopDTO1).isNotEqualTo(workshopDTO2);
        workshopDTO2.setId(workshopDTO1.getId());
        assertThat(workshopDTO1).isEqualTo(workshopDTO2);
        workshopDTO2.setId(2L);
        assertThat(workshopDTO1).isNotEqualTo(workshopDTO2);
        workshopDTO1.setId(null);
        assertThat(workshopDTO1).isNotEqualTo(workshopDTO2);
    }
}
