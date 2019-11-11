package be.sandervl.kranzenzo.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import be.sandervl.kranzenzo.web.rest.TestUtil;

public class WorkshopSubscriptionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkshopSubscription.class);
        WorkshopSubscription workshopSubscription1 = new WorkshopSubscription();
        workshopSubscription1.setId(1L);
        WorkshopSubscription workshopSubscription2 = new WorkshopSubscription();
        workshopSubscription2.setId(workshopSubscription1.getId());
        assertThat(workshopSubscription1).isEqualTo(workshopSubscription2);
        workshopSubscription2.setId(2L);
        assertThat(workshopSubscription1).isNotEqualTo(workshopSubscription2);
        workshopSubscription1.setId(null);
        assertThat(workshopSubscription1).isNotEqualTo(workshopSubscription2);
    }
}
