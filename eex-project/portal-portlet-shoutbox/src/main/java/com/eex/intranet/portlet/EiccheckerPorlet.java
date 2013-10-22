package com.eex.intranet.portlet;

import com.eex.intranet.portlet.helper.ENTSO_E_algorithm;
import com.vaadin.Application;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

@SuppressWarnings( "serial" )
public class EiccheckerPorlet extends Application
{
	private final Window mainWindow = new Window();
	private final CssLayout cssContent = new CssLayout();
	//compoments of eic checker
	private final TextField txtEic = new TextField();
	private final Label lblArea = new Label();
	private final Label lblMesg = new Label();
	private final Button btnPrufen = new Button();
	private final CssLayout cssEic = new CssLayout();
	private boolean flag = true;

	@Override
	public void init()
	{
		mainWindow.removeAllComponents();
		setMainWindow( mainWindow );
		createEIC_Checker();
		mainWindow.addComponent( cssContent );
	}

	private void createEIC_Checker()
	{
		cssContent.setSizeFull();
		cssContent.setImmediate( true );
		cssContent.setWidth( "310px" );
		cssContent.setHeight( "120px" );
		cssContent.setStyleName( "eic_cssContent" );
		cssContent.removeAllComponents();
		final Label lblEic = new Label( "EIC Checker" );
		lblEic.setStyleName( "eic_lblEic" );

		cssEic.removeAllComponents();
		cssEic.setStyleName( "eic_cssEic" );

		lblMesg.setStyleName( "eic_lblMesg" );

		txtEic.setMaxLength( 16 );
		txtEic.setInputPrompt( "EIC" );
		txtEic.setImmediate( true );
		txtEic.setStyleName( "eic_txteic" );
		txtEic.removeStyleName( "eic_txteic_empty" );

		txtEic.addValidator( new RegexpValidator( "[0-9a-zA-Z -]{16}", "" ) );

		txtEic.addShortcutListener( new ShortcutListener( "name", ShortcutAction.KeyCode.ENTER, null )
		{

			@Override
			public void handleAction( final Object sender, final Object target )
			{
				if ( target == txtEic )
				{
					eicCheck();
				}

			}
		} );

		lblArea.setStyleName( "eic_lblArea" );
		lblArea.addStyleName( "eic_display_none" );

		btnPrufen.setCaption( "Prüfen" );
		btnPrufen.setImmediate( true );
		btnPrufen.setStyleName( "eic_btnPrufen" );
		btnPrufen.addListener( new ClickListener()
		{

			@Override
			public void buttonClick( final ClickEvent event )
			{
				eicCheck();
			}
		} );

		cssEic.addComponent( txtEic );
		cssEic.addComponent( lblArea );
		cssEic.addComponent( btnPrufen );
		cssEic.addComponent( lblMesg );
		cssContent.addComponent( lblEic );
		cssContent.addComponent( cssEic );

	}

	private void eicCheck()
	{
		if ( flag )
		{
			if ( !txtEic.isValid() )
			{
				//Please enter 16 character alphanumeric or the character '-' 
				lblMesg.setValue( "Bitte geben Sie einen 16-stelligen Code oder '-' ein." );
				lblMesg.addStyleName( "eic_lblMesg_error" );
				txtEic.addStyleName( "eic_txteic_empty" );
			}
			else
			{
				//apply lower to upper case
				final String s = txtEic.getValue().toString().toUpperCase();
				txtEic.setValue( s );
				int country = 0;
				try
				{
					country = Integer.parseInt( s.substring( 0, 2 ) );
				}
				catch ( final Exception e )
				{
					txtEic.addStyleName( "eic_txteic_empty" );
					//Please enter the first two character is numberic
					lblMesg.setValue( "Die ersten zwei Stellen müssen numerische Character sein." );
					lblMesg.addStyleName( "eic_lblMesg_error" );
					return;
				}
				flag = checkENTSO_E( s, country );
			}
		}
		else
		{
			flag = true;

			txtEic.setValue( "" );
			btnPrufen.setCaption( "Prüfen" );
			txtEic.removeStyleName( "eic_display_none" );
			txtEic.removeStyleName( "eic_txteic_empty" );
			lblArea.addStyleName( "eic_display_none" );
			lblMesg.removeStyleName( "eic_lblMesg_error" );
			lblMesg.removeStyleName( "eic_lblMesg_valid" );

		}
	}

	private boolean checkENTSO_E( final String s, final int country )
	{
		if ( s.substring( 15 ).equals( "-" ) )
		{
			txtEic.addStyleName( "eic_txteic_empty" );
			// Please enter the last character not character '-'
			lblMesg.setValue( "Der letzte Character darf kein '-' sein. " );
			lblMesg.addStyleName( "eic_lblMesg_error" );
			return true;
		}
		else
		{
			//the eic code is valid
			if ( ENTSO_E_algorithm.checkENTSO_E( s ) )
			{

				lblArea.setValue( ENTSO_E_algorithm.getCountrybyEIC( country ) );
				lblMesg.setValue( "Valid EIC" );
				lblMesg.removeStyleName( "eic_lblMesg_error" );
				lblMesg.addStyleName( "eic_lblMesg_valid" );

				btnPrufen.setCaption( "Neu" );
				lblArea.removeStyleName( "eic_display_none" );
				txtEic.addStyleName( "eic_display_none" );

				return false;
			}
			else
			{

				lblMesg.setValue( "Invalid EIC" );
				lblMesg.addStyleName( "eic_lblMesg_error" );
				return true;
			}
		}
	}

}
