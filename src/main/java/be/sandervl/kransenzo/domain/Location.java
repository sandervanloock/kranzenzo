package be.sandervl.kransenzo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "location")
public class Location implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "latitude", nullable = false)
	private Float latitude;

	@NotNull
	@Column(name = "longitude", nullable = false)
	private Float longitude;

	@Column(name = "description")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude( Float latitude ) {
		this.latitude = latitude;
	}

	public Location latitude( Float latitude ) {
		this.latitude = latitude;
		return this;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude( Float longitude ) {
		this.longitude = longitude;
	}

	public Location longitude( Float longitude ) {
		this.longitude = longitude;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public Location description( String description ) {
		this.description = description;
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
		Location location = (Location) o;
		if ( location.getId() == null || getId() == null ) {
			return false;
		}
		return Objects.equals( getId(), location.getId() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getId() );
	}

	@Override
	public String toString() {
		return "Location{" +
				"id=" + getId() +
				", latitude='" + getLatitude() + "'" +
				", longitude='" + getLongitude() + "'" +
				", description='" + getDescription() + "'" +
				"}";
	}
}
