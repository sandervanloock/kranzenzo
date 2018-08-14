package be.sandervl.kransenzo.domain;

import be.sandervl.kransenzo.domain.enumeration.SubscriptionState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A WorkshopSubscription.
 */
@Entity
@Table(name = "workshop_subscription")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "workshopsubscription")
public class WorkshopSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private ZonedDateTime created;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SubscriptionState state;

    @ManyToOne
    private WorkshopDate workshop;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public WorkshopSubscription created( ZonedDateTime created ) {
        this.created = created;
        return this;
    }

    public SubscriptionState getState() {
        return state;
    }

    public void setState( SubscriptionState state ) {
        this.state = state;
    }

    public WorkshopSubscription state( SubscriptionState state ) {
        this.state = state;
        return this;
    }

    public WorkshopDate getWorkshop() {
        return workshop;
    }

    public void setWorkshop( WorkshopDate workshopDate ) {
        this.workshop = workshopDate;
    }

    public WorkshopSubscription workshop( WorkshopDate workshopDate ) {
        this.workshop = workshopDate;
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser( User user ) {
        this.user = user;
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
        WorkshopSubscription workshopSubscription = (WorkshopSubscription) o;
        if ( workshopSubscription.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), workshopSubscription.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "WorkshopSubscription{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
