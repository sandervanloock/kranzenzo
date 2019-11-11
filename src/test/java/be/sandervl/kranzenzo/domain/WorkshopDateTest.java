package be.sandervl.kranzenzo.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import be.sandervl.kranzenzo.web.rest.TestUtil;

public class WorkshopDateTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkshopDate.class);
        WorkshopDate workshopDate1 = new WorkshopDate();
        workshopDate1.setId(1L);
        WorkshopDate workshopDate2 = new WorkshopDate();
        workshopDate2.setId(workshopDate1.getId());
        assertThat(workshopDate1).isEqualTo(workshopDate2);
        workshopDate2.setId(2L);
        assertThat(workshopDate1).isNotEqualTo(workshopDate2);
        workshopDate1.setId(null);
        assertThat(workshopDate1).isNotEqualTo(workshopDate2);
    }
}
