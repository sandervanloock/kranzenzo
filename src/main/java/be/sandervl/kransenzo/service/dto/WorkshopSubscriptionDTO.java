package be.sandervl.kransenzo.service.dto;

import be.sandervl.kransenzo.domain.enumeration.SubscriptionState;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the WorkshopSubscription entity.
 */
public class WorkshopSubscriptionDTO implements Serializable {

    private Long id;

    private ZonedDateTime created;

    private SubscriptionState state;

    private Long workshopId;

    private String workshopDate;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated( ZonedDateTime created ) {
        this.created = created;
    }

    public SubscriptionState getState() {
        return state;
    }

    public void setState( SubscriptionState state ) {
        this.state = state;
    }

    public Long getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId( Long workshopDateId ) {
        this.workshopId = workshopDateId;
    }

    public String getWorkshopDate() {
        return workshopDate;
    }

    public void setWorkshopDate( String workshopDateDate ) {
        this.workshopDate = workshopDateDate;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        WorkshopSubscriptionDTO workshopSubscriptionDTO = (WorkshopSubscriptionDTO) o;
        if ( workshopSubscriptionDTO.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), workshopSubscriptionDTO.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "WorkshopSubscriptionDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
