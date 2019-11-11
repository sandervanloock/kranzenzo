package be.sandervl.kranzenzo.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the Homepage settings entity.
 */
public class HomepageSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @Size(max = 5000)
    private String description;

    private ImageDTO image;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage( ImageDTO image ) {
        this.image = image;
    }
}
