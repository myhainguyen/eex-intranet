/*
 * Dies ist UNVERÖFFENTLICHTER URHEBERRECHTLICH GESCHÜTZTER QUELLCODE der EWERK
 * IT GmbH, der Inhalt dieser Datei darf ohne die ausdrückliche Erlaubnis der
 * EWERK IT GmbH nicht an Dritte weitergegeben, kopiert oder in sonstiger Form,
 * im Ganzen oder in Teilen weitergegeben werden. Eine Weitergabe an Dritte,
 * oder Veränderungen am Programm dürfen nur mit ausdrücklicher Genehmigung der
 * EWERK IT GmbH erfolgen. Copyright 2002-2011, EWERK IT GmbH, Alle Rechte
 * vorbehalten.
 *
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of EWERK IT GmbH, the contents of
 * this file may not be disclosed to third parties, copied or duplicated in any
 * form, in whole or in part, without the prior written permission of EWERK IT
 * GmbH. Distribution to a third party or changes at the data file are only
 * allowed with the explicit permission of EWERK IT GmbH. Copyright 2002-2011,
 * EWERK IT GmbH, All rights reserved
 */

package de.eex.intranet.portal.portlet.sms;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import com.vaadin.Application;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.eex.intranet.portal.model.Sms;
import de.eex.intranet.portal.portlet.sms.view.SmsSendForm;
import de.eex.intranet.portal.portlet.sms.view.SmsSendFormReadyListener;
import de.eex.intranet.portal.services.SmsServiceException;

/**
 * User: mheineck Date: 29.09.13 Time: 18:31
 */
@Configurable( preConstruction = true )
public class SmsPortlet extends Application implements SmsSendFormReadyListener
{

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final Logger LOGGER = LoggerFactory.getLogger( SmsPortlet.class );
	private static final String HEADLINE = "SMS Senden";
	//@Qualifier( "SMSMessengerService" )

	//private SMSMessengerService smsMessengerService;

	//	private ApplicationContext context;
	private VerticalLayout mainLayout;
	private Window mainWindow;
	private CssLayout cssContent;

	@Override
	public void init()
	{

		LOGGER.info( "starting smsPortlet initial layout" );

		mainWindow = new Window( "Sms Portlet" );
		setMainWindow( mainWindow );
		cssContent = new CssLayout();
		cssContent.setSizeFull();
		cssContent.setImmediate( true );
		cssContent.setWidth( "310px" );
		cssContent.setHeight( "200px" );
		cssContent.setStyleName( "sms_cssContent" );
		cssContent.removeAllComponents();

		mainLayout = new VerticalLayout();
		mainLayout.setMargin( true );

		cssContent.addComponent( mainLayout );
		mainWindow.setContent( cssContent );

		Label smsHeadline = new Label();
		smsHeadline.setLocale( DEFAULT_LOCALE );
		smsHeadline.setValue( HEADLINE );
		/*		smsHeadline.setWidth( "-1px" );
				smsHeadline.setHeight( "-1px" );*/
		smsHeadline.setVisible( true );
		smsHeadline.setStyleName( "sms_label" );

		mainLayout.addComponent( smsHeadline );
		SmsSendForm smsSendForm = new SmsSendForm();
		smsSendForm.addListener( this );

		mainLayout.addComponent( smsSendForm );

	}

	@Override
	public void onCommitSMS( final Sms sms )
	{
		LOGGER.info( "invoking sms delivery to: {}", sms.getCompletePhoneNumber( '/' ) );
		StringBuilder stringBuilder = new StringBuilder();
		try
		{
			SMSMessengerServiceImpl smsMessengerService = new SMSMessengerServiceImpl();
			smsMessengerService.sendSMSToProvider( sms );
			stringBuilder.append( "Sms successfully sent to Number: " );
			stringBuilder.append( sms.getCompletePhoneNumber( '/' ) );
			mainWindow.showNotification( stringBuilder.toString() );
		}
		catch ( SmsServiceException e )
		{
			LOGGER.error( "Sms send failure. Sms : {}", sms.getCompletePhoneNumber( '/' ), e );
			stringBuilder.append( "Sms delivery failed !" );
			stringBuilder.append( sms.getCompletePhoneNumber( '/' ) );
			mainWindow.showNotification( stringBuilder.toString() );
		}
	}

	@Override
	public void onInvalid( final Sms sms )
	{
		mainWindow.showNotification( "Please verify your input. Sms data input is invalid." );
	}
}
