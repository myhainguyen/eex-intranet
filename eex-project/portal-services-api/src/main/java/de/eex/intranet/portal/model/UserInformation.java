package de.eex.intranet.portal.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "user_information" )
public class UserInformation implements Serializable
{
	@Id
	@Column( name = "id" )
	@GeneratedValue
	private Integer id;

	@Column( name = "createDate" )
	@Type( type = "timestamp" )
	private Date createDate;

	@Column( name = "modifiedDate" )
	@Type( type = "timestamp" )
	private Date modifiedDate;

	@Column( name = "creator" )
	private String creator;

	@Column( name = "info_text" )
	private String infoText;

	@Column( name = "filename" )
	private String fileName;

	@Column( name = "isDeleted", columnDefinition = "BIT", length = 1 )
	private boolean deleted;

	public Integer getId()
	{
		return id;
	}

	public void setId( final Integer id )
	{
		this.id = id;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate( final Date createDate )
	{
		this.createDate = createDate;
	}

	public Date getModifiedDate()
	{
		return modifiedDate;
	}

	public void setModifiedDate( final Date modifiedDate )
	{
		this.modifiedDate = modifiedDate;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator( final String creator )
	{
		this.creator = creator;
	}

	public String getInfoText()
	{
		return infoText;
	}

	public void setInfoText( final String infoText )
	{
		this.infoText = infoText;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName( final String fileName )
	{
		this.fileName = fileName;
	}

	public boolean isDeleted()
	{
		return deleted;
	}

	public void setDeleted( final boolean deleted )
	{
		this.deleted = deleted;
	}
}
