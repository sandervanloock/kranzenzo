package be.sandervl.kranzenzo.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "homepage")
    private Boolean homepage;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Workshop> workshops = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(unique = true)
    private Image image;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Tag name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isHomepage() {
        return homepage;
    }

    public Tag homepage(Boolean homepage) {
        this.homepage = homepage;
        return this;
    }

    public void setHomepage(Boolean homepage) {
        this.homepage = homepage;
    }

    public Set<Workshop> getWorkshops() {
        return workshops;
    }

    public Tag workshops(Set<Workshop> workshops) {
        this.workshops = workshops;
        return this;
    }

    public Tag addWorkshops(Workshop workshop) {
        this.workshops.add(workshop);
        workshop.getTags().add(this);
        return this;
    }

    public Tag removeWorkshops(Workshop workshop) {
        this.workshops.remove(workshop);
        workshop.getTags().remove(this);
        return this;
    }

    public void setWorkshops(Set<Workshop> workshops) {
        this.workshops = workshops;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Tag products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Tag addProducts(Product product) {
        this.products.add(product);
        product.getTags().add(this);
        return this;
    }

    public Tag removeProducts(Product product) {
        this.products.remove(product);
        product.getTags().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Image getImage() {
        return image;
    }

    public Tag image(Image Image) {
        this.image = Image;
        return this;
    }

    public void setImage(Image Image) {
        this.image = Image;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", homepage='" + isHomepage() + "'" +
            "}";
    }
}
