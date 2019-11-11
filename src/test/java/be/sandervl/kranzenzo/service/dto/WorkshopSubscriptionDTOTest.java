package be.sandervl.kranzenzo.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import be.sandervl.kranzenzo.web.rest.TestUtil;

public class WorkshopSubscriptionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkshopSubscriptionDTO.class);
        WorkshopSubscriptionDTO workshopSubscriptionDTO1 = new WorkshopSubscriptionDTO();
        workshopSubscriptionDTO1.setId(1L);
        WorkshopSubscriptionDTO workshopSubscriptionDTO2 = new WorkshopSubscriptionDTO();
        assertThat(workshopSubscriptionDTO1).isNotEqualTo(workshopSubscriptionDTO2);
        workshopSubscriptionDTO2.setId(workshopSubscriptionDTO1.getId());
        assertThat(workshopSubscriptionDTO1).isEqualTo(workshopSubscriptionDTO2);
        workshopSubscriptionDTO2.setId(2L);
        assertThat(workshopSubscriptionDTO1).isNotEqualTo(workshopSubscriptionDTO2);
        workshopSubscriptionDTO1.setId(null);
        assertThat(workshopSubscriptionDTO1).isNotEqualTo(workshopSubscriptionDTO2);
    }
}
