package be.sandervl.kranzenzo.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    private Float price;

    @Size(max = 5000)
    private String description;

    private Boolean isActive;

    @Min(value = 0)
    @Max(value = 10)
    private Integer numberOfBatteries;

    private Set <TagDTO> tags = new HashSet <>();

    private Set<ImageDTO> images = new HashSet<>();

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

    public Float getPrice() {
        return price;
    }

    public void setPrice( Float price ) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive( Boolean isActive ) {
        this.isActive = isActive;
    }

    public Integer getNumberOfBatteries() {
        return numberOfBatteries;
    }

    public void setNumberOfBatteries( Integer numberOfBatteries ) {
        this.numberOfBatteries = numberOfBatteries;
    }

    public Set <TagDTO> getTags() {
        return tags;
    }

    public void setTags( Set <TagDTO> tags ) {
        this.tags = tags;
    }

    public Set <ImageDTO> getImages() {
        return images;
    }

    public void setImages( Set <ImageDTO> images ) {
        this.images = images;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if ( productDTO.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), productDTO.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", numberOfBatteries=" + getNumberOfBatteries() +
            "}";
    }
}
