package be.sandervl.kranzenzo.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import be.sandervl.kranzenzo.web.rest.TestUtil;

public class WorkshopTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Workshop.class);
        Workshop workshop1 = new Workshop();
        workshop1.setId(1L);
        Workshop workshop2 = new Workshop();
        workshop2.setId(workshop1.getId());
        assertThat(workshop1).isEqualTo(workshop2);
        workshop2.setId(2L);
        assertThat(workshop1).isNotEqualTo(workshop2);
        workshop1.setId(null);
        assertThat(workshop1).isNotEqualTo(workshop2);
    }
}
