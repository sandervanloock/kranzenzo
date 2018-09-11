package be.sandervl.kranzenzo.service.mapper;

import be.sandervl.kranzenzo.domain.Customer;
import be.sandervl.kranzenzo.service.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, UserMapper.class})
public interface CustomerMapper extends EntityMapper <CustomerDTO, Customer> {

    @Mapping(target = "latitude", source = "address.latitude")
    @Mapping(target = "longitude", source = "address.longitude")
    @Mapping(target = "description", source = "address.description")
    @Mapping(source = "user", target = "user", qualifiedByName = "userToUserDTO")
    CustomerDTO toDto( Customer customer );

    @Mapping(source = "user", target = "user", qualifiedByName = "userDTOToUser")
    @Mapping(source = "latitude", target = "address.latitude")
    @Mapping(source = "longitude", target = "address.longitude")
    @Mapping(source = "description", target = "address.description")
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
