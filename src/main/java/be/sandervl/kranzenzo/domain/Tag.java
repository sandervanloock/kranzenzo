package be.sandervl.kranzenzo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set <Workshop> workshops = new HashSet <>();

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set <Product> products = new HashSet <>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Tag name( String name ) {
        this.name = name;
        return this;
    }

    public Set <Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops( Set <Workshop> workshops ) {
        this.workshops = workshops;
    }

    public Tag workshops( Set <Workshop> workshops ) {
        this.workshops = workshops;
        return this;
    }

    public Tag addWorkshops( Workshop workshop ) {
        this.workshops.add( workshop );
        workshop.getTags().add( this );
        return this;
    }

    public Tag removeWorkshops( Workshop workshop ) {
        this.workshops.remove( workshop );
        workshop.getTags().remove( this );
        return this;
    }

    public Set <Product> getProducts() {
        return products;
    }

    public void setProducts( Set <Product> products ) {
        this.products = products;
    }

    public Tag products( Set <Product> products ) {
        this.products = products;
        return this;
    }

    public Tag addProducts( Product product ) {
        this.products.add( product );
        product.getTags().add( this );
        return this;
    }

    public Tag removeProducts( Product product ) {
        this.products.remove( product );
        product.getTags().remove( this );
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
        Tag tag = (Tag) o;
        if ( tag.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), tag.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
