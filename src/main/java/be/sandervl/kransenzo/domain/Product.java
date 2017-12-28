package be.sandervl.kransenzo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "product")
public class Product implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", nullable = false)
    private Float price;

    @Size(max = 5000)
    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Image> images = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Order> orders = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "product_tags",
            joinColumns = @JoinColumn(name = "products_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Product name( String name ) {
        this.name = name;
        return this;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public Product price( Float price ) {
        this.price = price;
        return this;
    }

    public void setPrice( Float price ) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public Product description( String description ) {
        this.description = description;
        return this;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Product isActive( Boolean isActive ) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive( Boolean isActive ) {
        this.isActive = isActive;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Product images( Set<Image> images ) {
        this.images = images;
        return this;
    }

    public Product addImages( Image image ) {
        this.images.add( image );
        image.setProduct( this );
        return this;
    }

    public Product removeImages( Image image ) {
        this.images.remove( image );
        image.setProduct( null );
        return this;
    }

    public void setImages( Set<Image> images ) {
        this.images = images;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Product orders( Set<Order> orders ) {
        this.orders = orders;
        return this;
    }

    public Product addOrders( Order order ) {
        this.orders.add( order );
        order.setProduct( this );
        return this;
    }

    public Product removeOrders( Order order ) {
        this.orders.remove( order );
        order.setProduct( null );
        return this;
    }

    public void setOrders( Set<Order> orders ) {
        this.orders = orders;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Product tags( Set<Tag> tags ) {
        this.tags = tags;
        return this;
    }

    public Product addTags( Tag tag ) {
        this.tags.add( tag );
        tag.getTags().add( this );
        return this;
    }

    public Product removeTags( Tag tag ) {
        this.tags.remove( tag );
        tag.getTags().remove( this );
        return this;
    }

    public void setTags( Set<Tag> tags ) {
        this.tags = tags;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Product product = (Product) o;
        if ( product.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), product.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", price='" + getPrice() + "'" +
                ", description='" + getDescription() + "'" +
                ", isActive='" + isIsActive() + "'" +
                "}";
    }
}
