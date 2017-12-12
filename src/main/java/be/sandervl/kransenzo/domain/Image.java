package be.sandervl.kransenzo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Image.
 */
@Entity
@Table(name = "image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "image")
public class Image implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Lob
	@Column(name = "data", nullable = false)
	private byte[] data;

	@Column(name = "data_content_type", nullable = false)
	private String dataContentType;

	@ManyToOne
	private Product product;

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
				", dataContentType='" + dataContentType + "'" +
				"}";
	}
}
