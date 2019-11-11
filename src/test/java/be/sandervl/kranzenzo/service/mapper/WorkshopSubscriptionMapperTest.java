package be.sandervl.kranzenzo.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class WorkshopSubscriptionMapperTest {

    private WorkshopSubscriptionMapper workshopSubscriptionMapper;

    @BeforeEach
    public void setUp() {
        workshopSubscriptionMapper = new WorkshopSubscriptionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(workshopSubscriptionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(workshopSubscriptionMapper.fromId(null)).isNull();
    }
}
