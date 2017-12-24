package be.sandervl.kransenzo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tag")
public class Tag implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@ManyToMany(mappedBy = "tags")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Product> tags = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Tag name( String name ) {
		this.name = name;
		return this;
	}

	public Set<Product> getTags() {
		return tags;
	}

	public void setTags( Set<Product> products ) {
		this.tags = products;
	}

	public Tag tags( Set<Product> products ) {
		this.tags = products;
		return this;
	}

	public Tag addTags( Product product ) {
		this.tags.add( product );
		product.getTags().add( this );
		return this;
	}

	public Tag removeTags( Product product ) {
		this.tags.remove( product );
		product.getTags().remove( this );
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
		Tag tag = (Tag) o;
		if ( tag.getId() == null || getId() == null ) {
			return false;
		}
		return Objects.equals( getId(), tag.getId() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getId() );
	}

	@Override
	public String toString() {
		return "Tag{" +
				"id=" + getId() +
				", name='" + getName() + "'" +
				"}";
	}
}