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

package de.eex.intranet.portal.portlet.sms.view;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.addon.beanvalidation.BeanValidationValidator;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.eex.intranet.portal.model.Sms;

/**
 * User: mheineck Date: 30.09.13 Time: 01:43
 */

public class SmsSendForm extends FormLayout implements Button.ClickListener
{
	private static final Logger LOGGER = LoggerFactory.getLogger( SmsSendForm.class );
	private static final int MAX_LETTERS = 130;
	private final TextField areaCodeField;
	private final TextField phoneNumber;
	private final TextField smsText;
	private final BeanItem<Sms> item;
	private final Button sendBtn;
	private final BeanValidationForm<Sms> form;
	private final Label countSmsLetters;
	private HorizontalLayout horizontalLayout;
	private SmsSendFormReadyListener smsSendFormReadyListener;
	private final CssLayout cssContent;

	public SmsSendForm()
	{

		this.item = new BeanItem<Sms>( new Sms() );
		this.cssContent = new CssLayout();
		this.cssContent.setSizeFull();
		this.cssContent.setImmediate( true );
		this.cssContent.setWidth( "-1px" );
		this.cssContent.setHeight( "-1px" );
		this.cssContent.setStyleName( "sms_csssms" );
		this.cssContent.removeAllComponents();
		this.cssContent.setSizeFull();

		this.areaCodeField = new TextField();
		//this.areaCodeField.setColumns( 8 );
		this.areaCodeField.setWidth( "60px" );

		this.phoneNumber = new TextField();
		this.phoneNumber.setImmediate( true );
		this.form = new BeanValidationForm<Sms>( Sms.class );
		form.setDebugId("frmSMS_id");
		form.setItemDataSource( item );
		form.setImmediate( true );

		this.smsText = new TextField();
		form.addField( "smsText", smsText );
		this.sendBtn = new Button( "Senden" );

		sendBtn.setHeight( "-1px" );
		sendBtn.setWidth( "60px" );
		sendBtn.setImmediate( true );
		sendBtn.addListener( this );
		sendBtn.setStyleName( "sms_btn" );

		//areaCodeField.addValidator(new BeanValidationValidator(Sms.class , "areaCode"));

		areaCodeField.setRequired( true );
		areaCodeField.setInputPrompt( "Vorwahl" );
		areaCodeField.setImmediate( true );
		areaCodeField.setWriteThrough( true );
		form.addField( "areaCode", areaCodeField );
		//areaCodeField.setWidth( "20px" );

		//phoneNumber.addValidator( new BeanValidationValidator( Sms.class, "phone" ) );

		phoneNumber.setInputPrompt( "Telefonnummer" );
		phoneNumber.setRequired( true );
		phoneNumber.setWidth( "184px" );
		phoneNumber.setImmediate( true );
		phoneNumber.setWriteThrough( true );
		form.addField( "phone", phoneNumber );

		//smsText.addValidator( new BeanValidationValidator( Sms.class, "smsText" ) );
		smsText.setInputPrompt( "Text" );
		smsText.setDebugId("smsText_id");
		smsText.setWidth( "256px" );
		smsText.setHeight( "-1px" );
		smsText.setRequired( true );
		smsText.setMaxLength( MAX_LETTERS );

		setSpacing( false );
		setWidth( "273px" );
		setHeight( "200px" );
		setSizeFull();

		VerticalLayout smsTextLayoutWrapper = new VerticalLayout();
		smsTextLayoutWrapper.setHeight( "-1px" );
		smsTextLayoutWrapper.setWidth( "256px" );
		smsTextLayoutWrapper.setStyleName( "sms_text_area" );
		smsTextLayoutWrapper.addComponent( smsText );
		smsTextLayoutWrapper.setComponentAlignment( smsText, Alignment.BOTTOM_CENTER );

		cssContent.addComponent( buildPhoneNumberLayout() );
		cssContent.addComponent( smsTextLayoutWrapper );

		addComponent( cssContent );

		countSmsLetters = new Label();
		countSmsLetters.setDebugId("countSmsLetters_id");
		countSmsLetters.setImmediate( true );
		countSmsLetters.setValue( buildStringForCounter( smsText.getValue().toString().length() ) );
		countSmsLetters.setStyleName( "count_sms_letters" );


		smsText.addListener( new FieldEvents.TextChangeListener()
		{
			@Override
			public void textChange( final FieldEvents.TextChangeEvent textChangeEvent )
			{

				countSmsLetters.setValue( buildStringForCounter( textChangeEvent.getText().length() ) );
			}
		} );

		smsText.setTextChangeEventMode( AbstractTextField.TextChangeEventMode.EAGER );
		smsText.setImmediate( true );
		smsText.setWriteThrough( true );

		//smsText.setColumns();

		final HorizontalLayout lastRow = new HorizontalLayout();
		lastRow.addComponent( countSmsLetters );
		lastRow.addComponent( sendBtn );
		lastRow.setWidth( "273px" );
		//lastRow.setComponentAlignment( countSmsLetters, Alignment.TOP_LEFT );
		lastRow.setComponentAlignment( sendBtn, Alignment.TOP_RIGHT );
		lastRow.setSpacing( true );

		cssContent.addComponent( lastRow );

	}

	private String buildStringForCounter( final int counterValue )
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append( Integer.toString( counterValue ) );
		stringBuilder.append( ' ' );
		stringBuilder.append( '/' );
		stringBuilder.append( ' ' );
		stringBuilder.append( Integer.toString( MAX_LETTERS ) );
		return stringBuilder.toString();
	}

	public void addListener( SmsSendFormReadyListener listener )
	{

		// TODO consider using list of those listeners
		this.smsSendFormReadyListener = listener;
	}

	@Override
	public void buttonClick( final Button.ClickEvent clickEvent )
	{
		installValidator( areaCodeField, "areaCode" );
		installValidator( phoneNumber, "phone" );
		installValidator( smsText, "smsText" );
		try
		{
			System.out.println(" failes here");
			form.commit(); // FIXME this just failes here,
			Sms sms = item.getBean();
			sms.setAreaCode( form.getField( "areaCode" ).getValue().toString() );
			sms.setPhone( form.getField( "phone" ).getValue().toString() );
			sms.setSmsText( form.getField( "smsText" ).getValue().toString() );
			if ( LOGGER.isDebugEnabled() )
			{
				System.out.println("hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
				LOGGER.debug( "invoking Listener methods for sending sms to: {}", sms.getCompletePhoneNumber( '/' ) );
			}
			smsSendFormReadyListener.onCommitSMS( sms );

		}
		catch ( Validator.InvalidValueException e )
		{
			if ( LOGGER.isDebugEnabled() )
			{
				LOGGER.debug( "SMS send form was invalid", e );
			}
			smsSendFormReadyListener.onInvalid( item.getBean() );
		}
	}

	public HorizontalLayout buildPhoneNumberLayout()
	{

		horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing( true );
		horizontalLayout.setWidth( "-1px" );
		horizontalLayout.setHeight( "-1px" );
		horizontalLayout.setVisible( true );

		horizontalLayout.addComponent( areaCodeField );
		horizontalLayout.addComponent( phoneNumber );

		return horizontalLayout;
	}

	private void installValidator( Field field, String attribute )
	{
		Collection<Validator> validators = field.getValidators();

		if ( validators == null || validators.isEmpty() )
		{

			field.addValidator( new BeanValidationValidator( Sms.class, attribute ) );
		}
	}

}
