package be.sandervl.kransenzo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A WorkshopDate.
 */
@Entity
@Table(name = "workshop_date")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "workshopdate")
public class WorkshopDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @OneToMany(mappedBy = "workshop")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set <WorkshopSubscription> subscriptions = new HashSet <>();

    @ManyToOne
    private Workshop workshop;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public WorkshopDate date( ZonedDateTime date ) {
        this.date = date;
        return this;
    }

    public Set <WorkshopSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions( Set <WorkshopSubscription> workshopSubscriptions ) {
        this.subscriptions = workshopSubscriptions;
    }

    public WorkshopDate subscriptions( Set <WorkshopSubscription> workshopSubscriptions ) {
        this.subscriptions = workshopSubscriptions;
        return this;
    }

    public WorkshopDate addSubscriptions( WorkshopSubscription workshopSubscription ) {
        this.subscriptions.add( workshopSubscription );
        workshopSubscription.setWorkshop( this );
        return this;
    }

    public WorkshopDate removeSubscriptions( WorkshopSubscription workshopSubscription ) {
        this.subscriptions.remove( workshopSubscription );
        workshopSubscription.setWorkshop( null );
        return this;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public void setWorkshop( Workshop workshop ) {
        this.workshop = workshop;
    }

    public WorkshopDate workshop( Workshop workshop ) {
        this.workshop = workshop;
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
        WorkshopDate workshopDate = (WorkshopDate) o;
        if ( workshopDate.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), workshopDate.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "WorkshopDate{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
