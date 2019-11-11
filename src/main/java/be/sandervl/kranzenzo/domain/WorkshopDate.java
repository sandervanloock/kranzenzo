package be.sandervl.kranzenzo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A WorkshopDate.
 */
@Entity
@Table(name = "workshop_date")
public class WorkshopDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @OneToMany(mappedBy = "workshop")
    private Set<WorkshopSubscription> subscriptions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("workshopDates")
    private Workshop workshop;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public WorkshopDate date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Set<WorkshopSubscription> getSubscriptions() {
        return subscriptions;
    }

    public WorkshopDate subscriptions(Set<WorkshopSubscription> workshopSubscriptions) {
        this.subscriptions = workshopSubscriptions;
        return this;
    }

    public WorkshopDate addSubscriptions(WorkshopSubscription workshopSubscription) {
        this.subscriptions.add(workshopSubscription);
        workshopSubscription.setWorkshop(this);
        return this;
    }

    public WorkshopDate removeSubscriptions(WorkshopSubscription workshopSubscription) {
        this.subscriptions.remove(workshopSubscription);
        workshopSubscription.setWorkshop(null);
        return this;
    }

    public void setSubscriptions(Set<WorkshopSubscription> workshopSubscriptions) {
        this.subscriptions = workshopSubscriptions;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public WorkshopDate workshop(Workshop workshop) {
        this.workshop = workshop;
        return this;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkshopDate)) {
            return false;
        }
        return id != null && id.equals(((WorkshopDate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WorkshopDate{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
