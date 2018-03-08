package be.sandervl.kransenzo.service.mapper;

import be.sandervl.kransenzo.domain.Product;
import be.sandervl.kransenzo.service.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Product and its DTO ProductDTO.
 */
@Mapper(componentModel = "spring", uses = { TagMapper.class, ImageMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product>
{

    @Mapping(target = "images")
    @Mapping(target = "orders", ignore = true)
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id){
        if (id == null){
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
