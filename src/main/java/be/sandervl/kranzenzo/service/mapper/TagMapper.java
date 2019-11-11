package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.*;
import be.sandervl.kranzenzo.service.dto.TagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {

    @Mapping(source = "image.id", target = "imageId")
    TagDTO toDto(Tag tag);

    @Mapping(target = "workshops", ignore = true)
    @Mapping(target = "removeWorkshops", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProducts", ignore = true)
    @Mapping(source = "imageId", target = "image")
    Tag toEntity(TagDTO tagDTO);

    default Tag fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId(id);
        return tag;
    }
}
