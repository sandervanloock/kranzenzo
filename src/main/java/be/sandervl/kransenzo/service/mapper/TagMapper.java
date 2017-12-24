package be.sandervl.kransenzo.service.mapper;

import be.sandervl.kransenzo.domain.Tag;
import be.sandervl.kransenzo.service.dto.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Tag and its DTO TagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TagMapper extends EntityMapper<TagDTO, Tag>
{

	@Mapping(target = "tags", ignore = true)
	Tag toEntity( TagDTO tagDTO );

	default Tag fromId( Long id ) {
		if ( id == null ) {
			return null;
		}
		Tag tag = new Tag();
		tag.setId( id );
		return tag;
	}
}