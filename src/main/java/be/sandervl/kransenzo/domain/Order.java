package be.sandervl.kransenzo.domain;

import be.sandervl.kransenzo.domain.enumeration.DeliveryType;
import be.sandervl.kransenzo.domain.enumeration.OrderState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "order")
public class Order implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "created", nullable = false)
	private ZonedDateTime created;

	@Column(name = "updated")
	private ZonedDateTime updated;

	@Enumerated(EnumType.STRING)
	@Column(name = "state")
	private OrderState state;

	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_type")
	private DeliveryType deliveryType;

	@Column(name = "led")
	private Boolean led;

	@ManyToOne
	private Customer customer;

	@OneToOne
	@JoinColumn(unique = true)
	private Location deliveryAddress;

	@ManyToOne(optional = false)
	@NotNull
	private Product product;

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

	public Order created( ZonedDateTime created ) {
		this.created = created;
		return this;
	}

	public ZonedDateTime getUpdated() {
		return updated;
	}

	public void setUpdated( ZonedDateTime updated ) {
		this.updated = updated;
	}

	public Order updated( ZonedDateTime updated ) {
		this.updated = updated;
		return this;
	}

	public OrderState getState() {
		return state;
	}

	public void setState( OrderState state ) {
		this.state = state;
	}

	public Order state( OrderState state ) {
		this.state = state;
		return this;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType( DeliveryType deliveryType ) {
		this.deliveryType = deliveryType;
	}

	public Order deliveryType( DeliveryType deliveryType ) {
		this.deliveryType = deliveryType;
		return this;
	}

	public Boolean isLed() {
		return led;
	}

	public Order led( Boolean led ) {
		this.led = led;
		return this;
	}

	public void setLed( Boolean led ) {
		this.led = led;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer( Customer customer ) {
		this.customer = customer;
	}

	public Order customer( Customer customer ) {
		this.customer = customer;
		return this;
	}

	public Location getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress( Location location ) {
		this.deliveryAddress = location;
	}

	public Order deliveryAddress( Location location ) {
		this.deliveryAddress = location;
		return this;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct( Product product ) {
		this.product = product;
	}

	public Order product( Product product ) {
		this.product = product;
		return this;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		Order order = (Order) o;
		if ( order.getId() == null || getId() == null ) {
			return false;
		}
		return Objects.equals( getId(), order.getId() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getId() );
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + getId() +
				", created='" + getCreated() + "'" +
				", updated='" + getUpdated() + "'" +
				", state='" + getState() + "'" +
				", deliveryType='" + getDeliveryType() + "'" +
				", led='" + isLed() + "'" +
				"}";
	}
}
