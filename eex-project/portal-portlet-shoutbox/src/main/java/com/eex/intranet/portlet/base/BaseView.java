package com.eex.intranet.portlet.base;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.ui.ComponentContainer;

public abstract class BaseView<T extends ComponentContainer> implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog( getClass() );

	protected T content;

	public BaseView( final T layout )
	{
		setContent( layout );
		layout.setSizeFull();
	}

	/**
	 * Set a new content container. Default value is the object provided as
	 * parameter to the constructor.
	 * 
	 * @param content
	 */
	public void setContent( final T content )
	{
		this.content = content;
	}

	/**
	 * Get the content container. Default value is the object provided as
	 * parameter to the constructor.
	 * 
	 * @param content
	 */
	public T getContent()
	{
		return content;
	}
}