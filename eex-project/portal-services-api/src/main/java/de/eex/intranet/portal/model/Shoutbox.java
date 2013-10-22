package de.eex.intranet.portal.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "shoutbox" )
public class Shoutbox implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column( name = "sId" )
	@GeneratedValue
	private Integer id;

	//@Temporal( TemporalType.TIMESTAMP )
	@Column( name = "sDate" )
	@Type( type = "timestamp" )
	private Timestamp date;
	//@Type(type="timestamp")
	@Column( name = "sName" )
	private String name;

	@Column( name = "sMesg" )
	private String mesg;

	@Column( name = "sEditor" )
	private String editor;

	@Column( name = "sFlag_del" )
	private String flag_del;

	@Column( name = "sScreenName" )
	private String screenName;

	public Integer getId()
	{
		return id;
	}

	public void setId( final Integer id )
	{
		this.id = id;
	}

	public Timestamp getDate()
	{
		return date;
	}

	public void setDate( final Timestamp date )
	{
		this.date = date;
	}

	public String getName()
	{
		return name;
	}

	public void setName( final String name )
	{
		this.name = name;
	}

	public String getMesg()
	{
		return mesg;
	}

	public void setMesg( final String mesg )
	{
		this.mesg = mesg;
	}

	public String getEditor()
	{
		return editor;
	}

	public void setEditor( final String editor )
	{
		this.editor = editor;
	}

	public String getFlag_del()
	{
		return flag_del;
	}

	public void setFlag_del( final String flag_del )
	{
		this.flag_del = flag_del;
	}

	public String getScreenName()
	{
		return screenName;
	}

	public void setScreenName( final String screenName )
	{
		this.screenName = screenName;
	}

}
