package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.HomepageSettings;
import be.sandervl.kranzenzo.service.dto.HomepageSettingsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImageMapper.class,})
public interface HomepageSettingsMapper extends EntityMapper <HomepageSettingsDTO, HomepageSettings> {
    @Mapping(target = "image")
    HomepageSettingsDTO toDto( HomepageSettings customer );

    @Mapping(target = "image")
    HomepageSettings toEntity( HomepageSettingsDTO customerDTO );

    default HomepageSettings fromId( Long id ) {
        if ( id == null ) {
            return null;
        }
        HomepageSettings result = new HomepageSettings();
        result.setId( id );
        return result;
    }
}
