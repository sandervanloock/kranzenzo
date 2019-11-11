package be.sandervl.kranzenzo.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

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

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "number_of_batteries")
    private Integer numberOfBatteries;

    @OneToMany(mappedBy = "product")
    private Set<Image> images = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductOrder> orders = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "product_tags",
               joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

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

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public Product price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Product isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getNumberOfBatteries() {
        return numberOfBatteries;
    }

    public Product numberOfBatteries(Integer numberOfBatteries) {
        this.numberOfBatteries = numberOfBatteries;
        return this;
    }

    public void setNumberOfBatteries(Integer numberOfBatteries) {
        this.numberOfBatteries = numberOfBatteries;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Product images(Set<Image> images) {
        this.images = images;
        return this;
    }

    public Product addImages(Image image) {
        this.images.add(image);
        image.setProduct(this);
        return this;
    }

    public Product removeImages(Image image) {
        this.images.remove(image);
        image.setProduct(null);
        return this;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<ProductOrder> getOrders() {
        return orders;
    }

    public Product orders(Set<ProductOrder> productOrders) {
        this.orders = productOrders;
        return this;
    }

    public Product addOrders(ProductOrder productOrder) {
        this.orders.add(productOrder);
        productOrder.setProduct(this);
        return this;
    }

    public Product removeOrders(ProductOrder productOrder) {
        this.orders.remove(productOrder);
        productOrder.setProduct(null);
        return this;
    }

    public void setOrders(Set<ProductOrder> productOrders) {
        this.orders = productOrders;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Product tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Product addTags(Tag tag) {
        this.tags.add(tag);
        tag.getProducts().add(this);
        return this;
    }

    public Product removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getProducts().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", numberOfBatteries=" + getNumberOfBatteries() +
            "}";
    }
}
