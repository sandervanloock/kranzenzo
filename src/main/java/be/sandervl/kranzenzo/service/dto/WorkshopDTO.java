package be.sandervl.kranzenzo.service.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Workshop entity.
 */
public class WorkshopDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 5000)
    private String description;

    @Size(max = 5000)
    private String neededMaterials;

    @Size(max = 5000)
    private String includedInPrice;

    @NotNull
    private Integer durationInMinutes;

    @DecimalMin(value = "0")
    private Float price;

    private Integer maxSubscriptions;

    private Boolean isActive;

    private Boolean showOnHomepage;

    private Set <TagDTO> tags = new HashSet <>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getNeededMaterials() {
        return neededMaterials;
    }

    public void setNeededMaterials( String neededMaterials ) {
        this.neededMaterials = neededMaterials;
    }

    public String getIncludedInPrice() {
        return includedInPrice;
    }

    public void setIncludedInPrice( String includedInPrice ) {
        this.includedInPrice = includedInPrice;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes( Integer durationInMinutes ) {
        this.durationInMinutes = durationInMinutes;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice( Float price ) {
        this.price = price;
    }

    public Integer getMaxSubscriptions() {
        return maxSubscriptions;
    }

    public void setMaxSubscriptions( Integer maxSubscriptions ) {
        this.maxSubscriptions = maxSubscriptions;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive( Boolean isActive ) {
        this.isActive = isActive;
    }

    public Boolean isShowOnHomepage() {
        return showOnHomepage;
    }

    public void setShowOnHomepage( Boolean showOnHomepage ) {
        this.showOnHomepage = showOnHomepage;
    }

    public Set <TagDTO> getTags() {
        return tags;
    }

    public void setTags( Set <TagDTO> tags ) {
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

        WorkshopDTO workshopDTO = (WorkshopDTO) o;
        if ( workshopDTO.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), workshopDTO.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "WorkshopDTO{" +
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
