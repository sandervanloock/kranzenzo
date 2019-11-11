package be.sandervl.kranzenzo.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class WorkshopDateMapperTest {

    private WorkshopDateMapper workshopDateMapper;

    @BeforeEach
    public void setUp() {
        workshopDateMapper = new WorkshopDateMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(workshopDateMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(workshopDateMapper.fromId(null)).isNull();
    }
}
