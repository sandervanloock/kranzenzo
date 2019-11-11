package be.sandervl.kranzenzo.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Workshop.
 */
@Entity
@Table(name = "workshop")
public class Workshop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 5000)
    @Column(name = "description", length = 5000)
    private String description;

    @Size(max = 5000)
    @Column(name = "needed_materials", length = 5000)
    private String neededMaterials;

    @Size(max = 5000)
    @Column(name = "included_in_price", length = 5000)
    private String includedInPrice;

    @NotNull
    @Column(name = "duration_in_minutes", nullable = false)
    private Integer durationInMinutes;

    @DecimalMin(value = "0")
    @Column(name = "price")
    private Float price;

    @Column(name = "max_subscriptions")
    private Integer maxSubscriptions;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "show_on_homepage")
    private Boolean showOnHomepage;

    @OneToMany(mappedBy = "workshop")
    private Set<WorkshopDate> dates = new HashSet<>();

    @OneToMany(mappedBy = "workshop")
    private Set<Image> images = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "workshop_tags",
               joinColumns = @JoinColumn(name = "workshop_id", referencedColumnName = "id"),
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

    public Workshop name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Workshop description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNeededMaterials() {
        return neededMaterials;
    }

    public Workshop neededMaterials(String neededMaterials) {
        this.neededMaterials = neededMaterials;
        return this;
    }

    public void setNeededMaterials(String neededMaterials) {
        this.neededMaterials = neededMaterials;
    }

    public String getIncludedInPrice() {
        return includedInPrice;
    }

    public Workshop includedInPrice(String includedInPrice) {
        this.includedInPrice = includedInPrice;
        return this;
    }

    public void setIncludedInPrice(String includedInPrice) {
        this.includedInPrice = includedInPrice;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public Workshop durationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Float getPrice() {
        return price;
    }

    public Workshop price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getMaxSubscriptions() {
        return maxSubscriptions;
    }

    public Workshop maxSubscriptions(Integer maxSubscriptions) {
        this.maxSubscriptions = maxSubscriptions;
        return this;
    }

    public void setMaxSubscriptions(Integer maxSubscriptions) {
        this.maxSubscriptions = maxSubscriptions;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Workshop isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isShowOnHomepage() {
        return showOnHomepage;
    }

    public Workshop showOnHomepage(Boolean showOnHomepage) {
        this.showOnHomepage = showOnHomepage;
        return this;
    }

    public void setShowOnHomepage(Boolean showOnHomepage) {
        this.showOnHomepage = showOnHomepage;
    }

    public Set<WorkshopDate> getDates() {
        return dates;
    }

    public Workshop dates(Set<WorkshopDate> workshopDates) {
        this.dates = workshopDates;
        return this;
    }

    public Workshop addDates(WorkshopDate workshopDate) {
        this.dates.add(workshopDate);
        workshopDate.setWorkshop(this);
        return this;
    }

    public Workshop removeDates(WorkshopDate workshopDate) {
        this.dates.remove(workshopDate);
        workshopDate.setWorkshop(null);
        return this;
    }

    public void setDates(Set<WorkshopDate> workshopDates) {
        this.dates = workshopDates;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Workshop images(Set<Image> images) {
        this.images = images;
        return this;
    }

    public Workshop addImages(Image image) {
        this.images.add(image);
        image.setWorkshop(this);
        return this;
    }

    public Workshop removeImages(Image image) {
        this.images.remove(image);
        image.setWorkshop(null);
        return this;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Workshop tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Workshop addTags(Tag tag) {
        this.tags.add(tag);
        tag.getWorkshops().add(this);
        return this;
    }

    public Workshop removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getWorkshops().remove(this);
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
        if (!(o instanceof Workshop)) {
            return false;
        }
        return id != null && id.equals(((Workshop) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Workshop{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", neededMaterials='" + getNeededMaterials() + "'" +
            ", includedInPrice='" + getIncludedInPrice() + "'" +
            ", durationInMinutes=" + getDurationInMinutes() +
            ", price=" + getPrice() +
            ", maxSubscriptions=" + getMaxSubscriptions() +
            ", isActive='" + isIsActive() + "'" +
            ", showOnHomepage='" + isShowOnHomepage() + "'" +
            "}";
    }
}
