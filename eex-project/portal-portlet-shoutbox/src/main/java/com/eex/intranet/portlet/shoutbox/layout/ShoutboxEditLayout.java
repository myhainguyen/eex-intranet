package com.eex.intranet.portlet.shoutbox.layout;

import java.io.IOException;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import com.eex.intranet.portlet.base.BaseView;
import com.eex.intranet.portlet.helper.ApplicationConstant;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings( "serial" )
public class ShoutboxEditLayout extends BaseView<VerticalLayout>
{
	private TextField txtMesg;
	private final VerticalLayout mainWindow = new VerticalLayout();
	private final CssLayout cssContent = new CssLayout();
	private final PortletPreferences pref;

	private final Label lblError = new Label();
	private int limit;
	private Long userID;

	public Long getUserID()
	{
		return userID;
	}

	public void setUserID( final Long userID )
	{
		this.userID = userID;
	}

	public ShoutboxEditLayout( final PortletPreferences preferences )
	{
		super( new VerticalLayout() );
		setContent( mainWindow );
		mainWindow.setMargin( false );
		mainWindow.addComponent( cssContent );

		this.pref = preferences;

	}

	public void createLayout()
	{
		cssContent.removeAllComponents();
		cssContent.setWidth( "310px" );
		cssContent.setHeight( "140px" );
		cssContent.setStyleName( "shoutbox_cssContent" );

		final Label lblShoutbox = new Label( "Shoutbox" );
		lblShoutbox.setStyleName( "shoutbox_lblShoutbox" );

		txtMesg = new TextField();
		txtMesg.setCaption( "Anzahl angezeigter Nachrichten" );
		txtMesg.setValue( getLimit() );
		txtMesg.setStyleName( "shoutbox_txtMesg" );
		txtMesg.setImmediate( true );

		txtMesg.addShortcutListener( new ShortcutListener( "name", ShortcutAction.KeyCode.ENTER, null )
		{

			@Override
			public void handleAction( final Object sender, final Object target )
			{
				if ( target == txtMesg )
				{
					amountMesg();
				}

			}
		} );
		lblError.setStyleName( "shoutbox_lblError" );
		final Button btnSave = new Button( "Speichern" );
		btnSave.setStyleName( "shoutbox_btnSenden" );
		btnSave.addListener( new ClickListener()
		{

			@Override
			public void buttonClick( final ClickEvent event )
			{
				amountMesg();

			}
		} );
		cssContent.addComponent( lblShoutbox );

		cssContent.addComponent( txtMesg );
		cssContent.addComponent( btnSave );
		cssContent.addComponent( lblError );
	}

	private void amountMesg()
	{
		try
		{

			if ( txtMesg.getValue() != null )
			{

				limit = Integer.parseInt( txtMesg.getValue().toString() );
				if ( limit > 0 )
				{
					pref.setValue( ApplicationConstant.COUNT_MESG, txtMesg.getValue().toString() );
					// Saved successfully!
					lblError.setValue( "Erfolgreich gespeichert." );
					lblError.addStyleName( "shoutbox_success" );
					//save value change
					pref.store();

				}
				else
				{
					lblError.removeStyleName( "shoutbox_success" );
					lblError.setValue( "Bitte geben Sie eine Wert größer 0 ein." );
				}
			}
			else
			{
				//Please enter an integer greater than 0
				lblError.removeStyleName( "shoutbox_success" );
				lblError.setValue( "Bitte geben Sie eine Wert größer 0 ein." );
			}
			System.out.println( "txtMesg.getValue().toString():" + txtMesg.getValue().toString() );

		}
		catch ( final ReadOnlyException e )
		{
			System.out.println( "ReadOnlyException" );
			e.printStackTrace();
		}
		catch ( final ValidatorException e )
		{
			System.out.println( "ValidatorException" );
			e.printStackTrace();
		}
		catch ( final IOException e )
		{
			System.out.println( "IOException" );
			e.printStackTrace();
		}
		catch ( final Exception e )
		{
			System.out.println( "Exception" );
			lblError.removeStyleName( "shoutbox_success" );
			lblError.setValue( "Bitte geben Sie eine Wert größer 0 ein." );
		}
	}

	public int getLimit()
	{
		return limit;
	}

	public void setLimit( final int limit )
	{
		this.limit = limit;
	}

}
