package be.sandervl.kransenzo.domain;

import be.sandervl.kransenzo.domain.enumeration.DeliveryType;
import be.sandervl.kransenzo.domain.enumeration.OrderState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    public static final float PRICE_FOR_BATTERIES = 0.5f;
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private ZonedDateTime created = ZonedDateTime.now();

    @Column(name = "updated")
    private ZonedDateTime updated = ZonedDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OrderState state = OrderState.NEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType = DeliveryType.PICKUP;

    @Column(name = "include_batteries")
    private Boolean includeBatteries = false;

    @Size(max = 5000)
    @Column(name = "description", length = 5000)
    private String description;

    @DecimalMin(value = "0")
    @Column(name = "delivery_price")
    private Float deliveryPrice;

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

    public Order created( ZonedDateTime created ) {
        this.created = created;
        return this;
    }

    public void setCreated( ZonedDateTime created ) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public Order updated( ZonedDateTime updated ) {
        this.updated = updated;
        return this;
    }

    public void setUpdated( ZonedDateTime updated ) {
        this.updated = updated;
    }

    public OrderState getState() {
        return state;
    }

    public Order state( OrderState state ) {
        this.state = state;
        return this;
    }

    public void setState( OrderState state ) {
        this.state = state;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public Order deliveryType( DeliveryType deliveryType ) {
        this.deliveryType = deliveryType;
        return this;
    }

    public void setDeliveryType( DeliveryType deliveryType ) {
        this.deliveryType = deliveryType;
    }

    public Boolean isIncludeBatteries() {
        return includeBatteries;
    }

    public Order includeBatteries( Boolean includeBatteries ) {
        this.includeBatteries = includeBatteries;
        return this;
    }

    public void setIncludeBatteries( Boolean includeBatteries ) {
        this.includeBatteries = includeBatteries;
    }

    public String getDescription() {
        return description;
    }

    public Order description( String description ) {
        this.description = description;
        return this;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Float getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice( Float deliveryPrice ) {
        this.deliveryPrice = deliveryPrice;
    }

    public Order deliveryPrice( Float deliveryPrice ) {
        this.deliveryPrice = deliveryPrice;
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Order customer( Customer customer ) {
        this.customer = customer;
        return this;
    }

    public void setCustomer( Customer customer ) {
        this.customer = customer;
    }

    public Location getDeliveryAddress() {
        return deliveryAddress;
    }

    public Order deliveryAddress( Location location ) {
        this.deliveryAddress = location;
        return this;
    }

    public void setDeliveryAddress( Location location ) {
        this.deliveryAddress = location;
    }

    public Product getProduct() {
        return product;
    }

    public Order product( Product product ) {
        this.product = product;
        return this;
    }

    public void setProduct( Product product ) {
        this.product = product;
    }

    @Transient
    @JsonIgnore
    public float getTotalPrice() {
        Float result = product.getPrice();
        if ( deliveryType == DeliveryType.DELIVERED ) {
            result += this.deliveryPrice;
        }
        if ( includeBatteries ) {
            result += PRICE_FOR_BATTERIES;
        }
        return result;
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
                ", includeBatteries='" + isIncludeBatteries() + "'" +
                ", description='" + getDescription() + "'" +
                ", deliveryPrice='" + getDeliveryPrice() + "'" +
                "}";
    }
}
