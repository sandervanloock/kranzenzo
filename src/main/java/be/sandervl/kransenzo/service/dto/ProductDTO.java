package be.sandervl.kransenzo.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable
{

	private Long id;

	@NotNull
	private String name;

	private String description;

	@NotNull
	private Float price;

	private Set<TagDTO> tags = new HashSet<>();

	private Set<ImageDTO> images = new HashSet<>();

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

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice( Float price ) {
		this.price = price;
	}

	public Set<TagDTO> getTags() {
		return tags;
	}

	public void setTags( Set<TagDTO> tags ) {
		this.tags = tags;
	}

	public Set<ImageDTO> getImages() {
		return images;
	}

	public void setImages( Set<ImageDTO> images ) {
		this.images = images;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		ProductDTO productDTO = (ProductDTO) o;
		if ( productDTO.getId() == null || getId() == null ) {
			return false;
		}
		return Objects.equals( getId(), productDTO.getId() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getId() );
	}

	@Override
	public String toString() {
		return "ProductDTO{" +
				"id=" + getId() +
				", name='" + getName() + "'" +
				", description='" + getDescription() + "'" +
				", price='" + getPrice() + "'" +
				"}";
	}
}
