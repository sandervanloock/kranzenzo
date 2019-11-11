package be.sandervl.kranzenzo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import be.sandervl.kranzenzo.domain.enumeration.SubscriptionState;

/**
 * A WorkshopSubscription.
 */
@Entity
@Table(name = "workshop_subscription")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("workshopSubscriptions")
    private WorkshopDate workshop;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public WorkshopSubscription created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public SubscriptionState getState() {
        return state;
    }

    public WorkshopSubscription state(SubscriptionState state) {
        this.state = state;
        return this;
    }

    public void setState(SubscriptionState state) {
        this.state = state;
    }

    public WorkshopDate getWorkshop() {
        return workshop;
    }

    public WorkshopSubscription workshop(WorkshopDate workshopDate) {
        this.workshop = workshopDate;
        return this;
    }

    public void setWorkshop(WorkshopDate workshopDate) {
        this.workshop = workshopDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkshopSubscription)) {
            return false;
        }
        return id != null && id.equals(((WorkshopSubscription) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
