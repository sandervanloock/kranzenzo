package be.sandervl.kranzenzo.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import be.sandervl.kranzenzo.web.rest.TestUtil;

public class WorkshopDateDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkshopDateDTO.class);
        WorkshopDateDTO workshopDateDTO1 = new WorkshopDateDTO();
        workshopDateDTO1.setId(1L);
        WorkshopDateDTO workshopDateDTO2 = new WorkshopDateDTO();
        assertThat(workshopDateDTO1).isNotEqualTo(workshopDateDTO2);
        workshopDateDTO2.setId(workshopDateDTO1.getId());
        assertThat(workshopDateDTO1).isEqualTo(workshopDateDTO2);
        workshopDateDTO2.setId(2L);
        assertThat(workshopDateDTO1).isNotEqualTo(workshopDateDTO2);
        workshopDateDTO1.setId(null);
        assertThat(workshopDateDTO1).isNotEqualTo(workshopDateDTO2);
    }
}
