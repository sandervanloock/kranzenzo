package be.sandervl.kransenzo.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Image entity.
 */
public class ImageDTO implements Serializable
{

	private Long id;

	@NotNull
	@Lob
	private byte[] data;
	private String dataContentType;

	private Long productId;

	private String productName;

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

	public String getDataContentType() {
		return dataContentType;
	}

	public void setDataContentType( String dataContentType ) {
		this.dataContentType = dataContentType;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId( Long productId ) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName( String productName ) {
		this.productName = productName;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		ImageDTO imageDTO = (ImageDTO) o;
		if ( imageDTO.getId() == null || getId() == null ) {
			return false;
		}
		return Objects.equals( getId(), imageDTO.getId() );
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( getId() );
	}

	@Override
	public String toString() {
		return "ImageDTO{" +
				"id=" + getId() +
				", data='" + getData() + "'" +
				"}";
	}
}
