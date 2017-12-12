package be.sandervl.kransenzo.service.mapper;

import be.sandervl.kransenzo.domain.Order;
import be.sandervl.kransenzo.service.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Order and its DTO OrderDTO.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class, LocationMapper.class, ProductMapper.class, })
public interface OrderMapper extends EntityMapper<OrderDTO, Order>
{

	@Mapping(source = "customer.id", target = "customerId")

	@Mapping(source = "deliveryAddress.id", target = "deliveryAddressId")

	@Mapping(source = "product.id", target = "productId")
	@Mapping(source = "product.name", target = "productName")
	OrderDTO toDto( Order order );

	@Mapping(source = "customerId", target = "customer")

	@Mapping(source = "deliveryAddressId", target = "deliveryAddress")

	@Mapping(source = "productId", target = "product")
	Order toEntity( OrderDTO orderDTO );

	default Order fromId( Long id ) {
		if ( id == null ) {
			return null;
		}
		Order order = new Order();
		order.setId( id );
		return order;
	}
}
