package be.sandervl.kranzenzo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

/**
 * A Workshop.
 */
@Entity
@Table(name = "workshop")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    @OrderBy("jhi_date")
    private List <WorkshopDate> dates = new ArrayList <>(  );

    @OneToMany(mappedBy = "workshop")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set <Image> images = new HashSet <>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "workshop_tags",
        joinColumns = @JoinColumn(name = "workshops_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id"))
    private Set <Tag> tags = new HashSet <>();

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

    public Workshop name( String name ) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Workshop description( String description ) {
        this.description = description;
        return this;
    }

    public String getNeededMaterials() {
        return neededMaterials;
    }

    public void setNeededMaterials( String neededMaterials ) {
        this.neededMaterials = neededMaterials;
    }

    public Workshop neededMaterials( String neededMaterials ) {
        this.neededMaterials = neededMaterials;
        return this;
    }

    public String getIncludedInPrice() {
        return includedInPrice;
    }

    public void setIncludedInPrice( String includedInPrice ) {
        this.includedInPrice = includedInPrice;
    }

    public Workshop includedInPrice( String includedInPrice ) {
        this.includedInPrice = includedInPrice;
        return this;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes( Integer durationInMinutes ) {
        this.durationInMinutes = durationInMinutes;
    }

    public Workshop durationInMinutes( Integer durationInMinutes ) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice( Float price ) {
        this.price = price;
    }

    public Workshop price( Float price ) {
        this.price = price;
        return this;
    }

    public Integer getMaxSubscriptions() {
        return maxSubscriptions;
    }

    public void setMaxSubscriptions( Integer maxSubscriptions ) {
        this.maxSubscriptions = maxSubscriptions;
    }

    public Workshop maxSubscriptions( Integer maxSubscriptions ) {
        this.maxSubscriptions = maxSubscriptions;
        return this;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Workshop isActive( Boolean isActive ) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive( Boolean isActive ) {
        this.isActive = isActive;
    }

    public Boolean isShowOnHomepage() {
        return showOnHomepage;
    }

    public Workshop showOnHomepage( Boolean showOnHomepage ) {
        this.showOnHomepage = showOnHomepage;
        return this;
    }

    public void setShowOnHomepage( Boolean showOnHomepage ) {
        this.showOnHomepage = showOnHomepage;
    }

    public List <WorkshopDate> getDates() {
        return dates;
    }

    public void setDates( List<WorkshopDate> workshopDates ) {
        this.dates = workshopDates;
    }

    public Workshop dates( List <WorkshopDate> workshopDates ) {
        this.dates = workshopDates;
        return this;
    }

    public Workshop addDates( WorkshopDate workshopDate ) {
        this.dates.add( workshopDate );
        workshopDate.setWorkshop( this );
        return this;
    }

    public Workshop removeDates( WorkshopDate workshopDate ) {
        this.dates.remove( workshopDate );
        workshopDate.setWorkshop( null );
        return this;
    }

    public Set <Image> getImages() {
        return images;
    }

    public void setImages( Set <Image> images ) {
        this.images = images;
    }

    public Workshop images( Set <Image> images ) {
        this.images = images;
        return this;
    }

    public Workshop addImages( Image image ) {
        this.images.add( image );
        image.setWorkshop( this );
        return this;
    }

    public Workshop removeImages( Image image ) {
        this.images.remove( image );
        image.setWorkshop( null );
        return this;
    }

    public Set <Tag> getTags() {
        return tags;
    }

    public void setTags( Set <Tag> tags ) {
        this.tags = tags;
    }

    public Workshop tags( Set <Tag> tags ) {
        this.tags = tags;
        return this;
    }

    public Workshop addTags( Tag tag ) {
        this.tags.add( tag );
        tag.getWorkshops().add( this );
        return this;
    }

    public Workshop removeTags( Tag tag ) {
        this.tags.remove( tag );
        tag.getWorkshops().remove( this );
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
        Workshop workshop = (Workshop) o;
        if ( workshop.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), workshop.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
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
