package be.sandervl.kranzenzo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Image.
 */
@Entity
@Table(name = "image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "data_content_type", nullable = false)
    private String dataContentType;

    @ManyToOne
    @JsonIgnoreProperties("images")
    private Workshop workshop;

    @ManyToOne
    @JsonIgnoreProperties("images")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData( byte[] data ) {
        this.data = data;
    }

    public Image data( byte[] data ) {
        this.data = data;
        return this;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public void setDataContentType( String dataContentType ) {
        this.dataContentType = dataContentType;
    }

    public Image dataContentType( String dataContentType ) {
        this.dataContentType = dataContentType;
        return this;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public void setWorkshop( Workshop workshop ) {
        this.workshop = workshop;
    }

    public Image workshop( Workshop workshop ) {
        this.workshop = workshop;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct( Product product ) {
        this.product = product;
    }

    public Image product( Product product ) {
        this.product = product;
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
        Image image = (Image) o;
        if ( image.getId() == null || getId() == null ) {
            return false;
        }
        return Objects.equals( getId(), image.getId() );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( getId() );
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", dataContentType='" + getDataContentType() + "'" +
            "}";
    }
}
