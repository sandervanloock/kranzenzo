package be.sandervl.kransenzo.service.mapper;

import be.sandervl.kransenzo.domain.Customer;
import be.sandervl.kransenzo.service.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = { LocationMapper.class, UserMapper.class, })
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer>
{

	@Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "user.id", target = "userId")
	CustomerDTO toDto( Customer customer );

    @Mapping(source = "userId", target = "user", qualifiedByName = "userFromId")
    @Mapping(source = "addressId", target = "address")
    @Mapping(target = "orders", ignore = true)
    Customer toEntity( CustomerDTO customerDTO );

    default Customer fromId( Long id ) {
        if ( id == null ) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId( id );
        return customer;
    }
}
