package be.sandervl.kranzenzo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Min(value = 1000)
    @Max(value = 9999)
    @Column(name = "zip_code")
    private Integer zipCode;

    @Column(name = "province")
    private String province;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(unique = true)
    private Location address;

    @OneToMany(mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set <ProductOrder> orders = new HashSet <>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet( String street ) {
        this.street = street;
    }

    public Customer street( String street ) {
        this.street = street;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public Customer city( String city ) {
        this.city = city;
        return this;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode( Integer zipCode ) {
        this.zipCode = zipCode;
    }

    public Customer zipCode( Integer zipCode ) {
        this.zipCode = zipCode;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince( String province ) {
        this.province = province;
    }

    public Customer province( String province ) {
        this.province = province;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber ) {
        this.phoneNumber = phoneNumber;
    }

    public Customer phoneNumber( String phoneNumber ) {
        this.phoneNumber = phoneNumber;
        return this;
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

    public Set <ProductOrder> getOrders() {
        return orders;
    }

    public void setOrders( Set <ProductOrder> productOrders ) {
        this.orders = productOrders;
    }

    public Customer orders( Set <ProductOrder> productOrders ) {
        this.orders = productOrders;
        return this;
    }

    public Customer addOrders( ProductOrder productOrder ) {
        this.orders.add( productOrder );
        productOrder.setCustomer( this );
        return this;
    }

    public Customer removeOrders( ProductOrder productOrder ) {
        this.orders.remove( productOrder );
        productOrder.setCustomer( null );
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
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", zipCode=" + getZipCode() +
            ", province='" + getProvince() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
