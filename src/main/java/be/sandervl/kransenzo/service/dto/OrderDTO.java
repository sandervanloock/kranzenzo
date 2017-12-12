package be.sandervl.kransenzo.service.dto;

import be.sandervl.kransenzo.domain.enumeration.DeliveryType;
import be.sandervl.kransenzo.domain.enumeration.OrderState;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the Order entity.
 */
public class OrderDTO implements Serializable
{

	private Long id;

	@NotNull
	private ZonedDateTime created;

	private ZonedDateTime updated;

	private OrderState state;

	private DeliveryType deliveryType;

	private Boolean led;

	private Long customerId;

	private Long deliveryAddressId;

	private Long productId;

	private String productName;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated( ZonedDateTime created ) {
		this.created = created;
	}

	public ZonedDateTime getUpdated() {
		return updated;
	}

	public void setUpdated( ZonedDateTime updated ) {
		this.updated = updated;
	}

	public OrderState getState() {
		return state;
	}

	public void setState( OrderState state ) {
		this.state = state;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType( DeliveryType deliveryType ) {
		this.deliveryType = deliveryType;
	}

	public Boolean isLed() {
		return led;
	}

	public void setLed( Boolean led ) {
		this.led = led;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId( Long customerId ) {
		this.customerId = customerId;
	}

	public Long getDeliveryAddressId() {
		return deliveryAddressId;
	}

	public void setDeliveryAddressId( Long locationId ) {
		this.deliveryAddressId = locationId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId( Long productId ) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName( String productName ) {
		this.productName = productName;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		OrderDTO orderDTO = (OrderDTO) o;
		if ( orderDTO.getId() == null || getId() == null ) {
			return false;
		}
		return Objects.equals( getId(), orderDTO.getId() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getId() );
	}

	@Override
	public String toString() {
		return "OrderDTO{" +
				"id=" + getId() +
				", created='" + getCreated() + "'" +
				", updated='" + getUpdated() + "'" +
				", state='" + getState() + "'" +
				", deliveryType='" + getDeliveryType() + "'" +
				", led='" + isLed() + "'" +
				"}";
	}
}
