package be.sandervl.kranzenzo.domain;

import be.sandervl.kranzenzo.domain.enumeration.DeliveryType;
import be.sandervl.kranzenzo.domain.enumeration.OrderState;
import be.sandervl.kranzenzo.domain.enumeration.PaymentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import static be.sandervl.kranzenzo.config.Constants.PRICE_FOR_BATTERIES;

/**
 * A ProductOrder.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductOrder implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType = PaymentType.CASH;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private Customer customer;

    @OneToOne
    @JoinColumn(unique = true)
    private Location deliveryAddress;

    @Column(name = "pickup_date_time")
    private String pickupDateTime;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("orders")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public ProductOrder created( ZonedDateTime created ) {
        this.created = created;
        return this;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated( ZonedDateTime updated ) {
        this.updated = updated;
    }

    public ProductOrder updated( ZonedDateTime updated ) {
        this.updated = updated;
        return this;
    }

    public OrderState getState() {
        return state;
    }

    public void setState( OrderState state ) {
        this.state = state;
    }

    public ProductOrder state( OrderState state ) {
        this.state = state;
        return this;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType( DeliveryType deliveryType ) {
        this.deliveryType = deliveryType;
    }

    public ProductOrder deliveryType( DeliveryType deliveryType ) {
        this.deliveryType = deliveryType;
        return this;
    }

    public Boolean isIncludeBatteries() {
        return includeBatteries;
    }

    public ProductOrder includeBatteries( Boolean includeBatteries ) {
        this.includeBatteries = includeBatteries;
        return this;
    }

    public void setIncludeBatteries( Boolean includeBatteries ) {
        this.includeBatteries = includeBatteries;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public ProductOrder description( String description ) {
        this.description = description;
        return this;
    }

    public Float getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice( Float deliveryPrice ) {
        this.deliveryPrice = deliveryPrice;
    }

    public ProductOrder deliveryPrice( Float deliveryPrice ) {
        this.deliveryPrice = deliveryPrice;
        return this;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType( PaymentType paymentType ) {
        this.paymentType = paymentType;
    }

    public ProductOrder paymentType( PaymentType paymentType ) {
        this.paymentType = paymentType;
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer( Customer customer ) {
        this.customer = customer;
    }

    public ProductOrder customer( Customer customer ) {
        this.customer = customer;
        return this;
    }

    public Location getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress( Location location ) {
        this.deliveryAddress = location;
    }

    public ProductOrder deliveryAddress( Location location ) {
        this.deliveryAddress = location;
        return this;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime( String pickupDateTime ) {
        this.pickupDateTime = pickupDateTime;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct( Product product ) {
        this.product = product;
    }

    @Transient
    @JsonIgnore
    public float getTotalPrice() {
        Float result = product.getPrice();
        if ( product.hasDiscount() ) {
            result -= product.getDiscountAmount();
        }
        if ( deliveryType == DeliveryType.DELIVERED ) {
            result += this.deliveryPrice;
        }
        if ( includeBatteries ) {
            result += PRICE_FOR_BATTERIES * product.getNumberOfBatteries();
        }
        return result;
    }

    public ProductOrder product( Product product ) {
        this.product = product;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        ProductOrder productOrder = (ProductOrder) o;
        if ( productOrder.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), productOrder.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", state='" + getState() + "'" +
            ", deliveryType='" + getDeliveryType() + "'" +
            ", includeBatteries='" + isIncludeBatteries() + "'" +
            ", description='" + getDescription() + "'" +
            ", deliveryPrice=" + getDeliveryPrice() +
            ", paymentType='" + getPaymentType() + "'" +
            "}";
    }
}
