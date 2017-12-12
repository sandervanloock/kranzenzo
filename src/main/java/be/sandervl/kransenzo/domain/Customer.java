package be.sandervl.kransenzo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customer")
public class Customer implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(unique = true)
	private Location address;

	@OneToMany(mappedBy = "customer")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Order> orders = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Location getAddress() {
		return address;
	}

	public void setAddress( Location location ) {
		this.address = location;
	}

	public Customer address( Location location ) {
		this.address = location;
		return this;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders( Set<Order> orders ) {
		this.orders = orders;
	}

	public Customer orders( Set<Order> orders ) {
		this.orders = orders;
		return this;
	}

	public Customer addOrders( Order order ) {
		this.orders.add( order );
		order.setCustomer( this );
		return this;
	}

	public Customer removeOrders( Order order ) {
		this.orders.remove( order );
		order.setCustomer( null );
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
		Customer customer = (Customer) o;
		if ( customer.getId() == null || getId() == null ) {
			return false;
		}
		return Objects.equals( getId(), customer.getId() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getId() );
	}

	@Override
	public String toString() {
		return "Customer{" +
				"id=" + getId() +
				"}";
	}
}
