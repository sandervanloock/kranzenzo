package be.sandervl.kranzenzo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import be.sandervl.kranzenzo.domain.enumeration.OrderState;

import be.sandervl.kranzenzo.domain.enumeration.DeliveryType;

import be.sandervl.kranzenzo.domain.enumeration.PaymentType;

/**
 * A ProductOrder.
 */
@Entity
@Table(name = "jhi_order")
public class ProductOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "updated")
    private ZonedDateTime updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OrderState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType;

    @Column(name = "include_batteries")
    private Boolean includeBatteries;

    @Size(max = 5000)
    @Column(name = "description", length = 5000)
    private String description;

    @DecimalMin(value = "0")
    @Column(name = "delivery_price")
    private Float deliveryPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("productOrders")
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(unique = true)
    private Location deliveryAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JsonIgnoreProperties("productOrders")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public ProductOrder created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public ProductOrder updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public OrderState getState() {
        return state;
    }

    public ProductOrder state(OrderState state) {
        this.state = state;
        return this;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public ProductOrder deliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
        return this;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Boolean isIncludeBatteries() {
        return includeBatteries;
    }

    public ProductOrder includeBatteries(Boolean includeBatteries) {
        this.includeBatteries = includeBatteries;
        return this;
    }

    public void setIncludeBatteries(Boolean includeBatteries) {
        this.includeBatteries = includeBatteries;
    }

    public String getDescription() {
        return description;
    }

    public ProductOrder description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getDeliveryPrice() {
        return deliveryPrice;
    }

    public ProductOrder deliveryPrice(Float deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
        return this;
    }

    public void setDeliveryPrice(Float deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public ProductOrder paymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ProductOrder customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Location getDeliveryAddress() {
        return deliveryAddress;
    }

    public ProductOrder deliveryAddress(Location location) {
        this.deliveryAddress = location;
        return this;
    }

    public void setDeliveryAddress(Location location) {
        this.deliveryAddress = location;
    }

    public Product getProduct() {
        return product;
    }

    public ProductOrder product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOrder)) {
            return false;
        }
        return id != null && id.equals(((ProductOrder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
