package be.sandervl.kransenzo.service.mapper;

import be.sandervl.kransenzo.domain.Image;
import be.sandervl.kransenzo.service.dto.ImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Image and its DTO ImageDTO.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, })
public interface ImageMapper extends EntityMapper<ImageDTO, Image>
{

	@Mapping(source = "product.id", target = "productId")
	@Mapping(source = "product.name", target = "productName")
	ImageDTO toDto( Image image );

	@Mapping(source = "productId", target = "product")
	Image toEntity( ImageDTO imageDTO );

	default Image fromId( Long id ) {
		if ( id == null ) {
			return null;
		}
		Image image = new Image();
		image.setId( id );
		return image;
	}
}
