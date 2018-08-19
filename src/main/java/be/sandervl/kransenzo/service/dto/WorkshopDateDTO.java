package be.sandervl.kransenzo.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the WorkshopDate entity.
 */
public class WorkshopDateDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime date;

    private Long workshopId;

    private String workshopName;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate( ZonedDateTime date ) {
        this.date = date;
    }

    public Long getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId( Long workshopId ) {
        this.workshopId = workshopId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName( String workshopName ) {
        this.workshopName = workshopName;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        WorkshopDateDTO workshopDateDTO = (WorkshopDateDTO) o;
        if ( workshopDateDTO.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), workshopDateDTO.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "WorkshopDateDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
