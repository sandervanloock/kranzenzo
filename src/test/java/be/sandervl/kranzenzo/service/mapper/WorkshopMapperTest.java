package be.sandervl.kranzenzo.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class WorkshopMapperTest {

    private WorkshopMapper workshopMapper;

    @BeforeEach
    public void setUp() {
        workshopMapper = new WorkshopMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(workshopMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(workshopMapper.fromId(null)).isNull();
    }
}
