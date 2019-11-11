package be.sandervl.kranzenzo.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link be.sandervl.kranzenzo.domain.Tag} entity.
 */
public class TagDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Boolean homepage;


    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isHomepage() {
        return homepage;
    }

    public void setHomepage(Boolean homepage) {
        this.homepage = homepage;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long ImageId) {
        this.imageId = ImageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TagDTO tagDTO = (TagDTO) o;
        if (tagDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tagDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TagDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", homepage='" + isHomepage() + "'" +
            ", image=" + getImageId() +
            "}";
    }
}
