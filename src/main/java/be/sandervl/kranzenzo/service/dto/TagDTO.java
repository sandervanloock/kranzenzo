package be.sandervl.kranzenzo.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Tag entity.
 */
public class TagDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Boolean homepage;
    private ImageDTO image;
    private Long parentId;
    private String parentName;

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

    public Boolean isHomepage() {
        return homepage;
    }
    public void setHomepage( Boolean homepage ) {
        this.homepage = homepage;
    }
    public ImageDTO getImage() {
        return image;
    }
    public void setImage( ImageDTO image ) {
        this.image = image;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId( Long tagId ) {
        this.parentId = tagId;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName( String tagName ) {
        this.parentName = tagName;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        TagDTO tagDTO = (TagDTO) o;
        if ( tagDTO.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), tagDTO.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "TagDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", homepage='" + isHomepage() + "'" +
            "}";
    }
}
