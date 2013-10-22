package de.eex.intranet.portal.portlet.hotnews;

import java.util.Date;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

import de.eex.intranet.portal.conf.ConfigurationKey;
import de.eex.intranet.portal.model.UserInformation;
import de.eex.intranet.portal.portlet.hotnews.listener.UploadListener;

/**
 * Layout to edit UserInformation object
 * 
 * @author hai.nguyen
 * 
 */
public class FrontEndEditView extends VerticalLayout
{
	private static final long serialVersionUID = -3219805570617298623L;

	private final HotNewsApplication app;
	private TextArea infoText;
	private Label fileName;
	private Upload upload;
	private final UserInformation userInfo;

	/**
	 * Constructor
	 * 
	 * @param app
	 *            {@link HotNewsApplication}
	 * @param userInfo
	 *            {@link UserInformation}
	 */
	public FrontEndEditView( final HotNewsApplication app, final UserInformation userInfo )
	{
		this.app = app;
		this.userInfo = userInfo;

		setWidth( 600, Sizeable.UNITS_PIXELS );
		init();
	}

	/**
	 * Initialize edit view
	 */
	public void init()
	{
		infoText = new TextArea( app.msg( "information.text" ) );
		infoText.setRequired( true );
		infoText.setWidth( 400, Sizeable.UNITS_PIXELS );

		fileName = new Label();
		fileName.setImmediate( true );
		fileName.setCaption( app.msg( "information.filename" ) );

		addComponent( infoText );
		addComponent( fileName );

		addActionButton();

		setData();
	}

	/**
	 * Set data for table
	 */
	private void setData()
	{
		if ( userInfo != null )
		{
			infoText.setValue( userInfo.getInfoText() );
			fileName.setValue( userInfo.getFileName() != null ? userInfo.getFileName() : "" );
		}
	}

	/**
	 * Initialize layout for action button
	 */
	private void addActionButton()
	{
		final HorizontalLayout buttonLayout = new HorizontalLayout();

		final Upload upload = new Upload( null, null );
		upload.setImmediate( true );
		final UploadListener uploadListener = new UploadListener(	fileName,
																	app.getConfigurationValue( ConfigurationKey.FILE_UPLOAD_LOCATION ) );
		upload.addListener( uploadListener );
		upload.setReceiver( uploadListener );

		final Button saveBtn = new Button( app.msg( "save.button" ) );
		saveBtn.addListener( ClickEvent.class, this, "btnSaveClick" );

		final Button cancelBtn = new Button( app.msg( "cancel.button" ) );
		cancelBtn.addListener( ClickEvent.class, this, "btnCancelClick" );

		buttonLayout.addComponent( upload );
		buttonLayout.addComponent( saveBtn );
		buttonLayout.addComponent( cancelBtn );

		addComponent( buttonLayout );
	}

	/**
	 * Action event for save button
	 * 
	 * @param event
	 */
	public void btnSaveClick( final ClickEvent event )
	{
		if ( infoText.isValid() )
		{
			final UserInformation obj = new UserInformation();
			if ( userInfo == null )
			{
				obj.setCreator( app.getLiferayUser().getScreenName() );
				obj.setCreateDate( new Date() );
			}
			else
			{
				obj.setId( userInfo.getId() );
				obj.setCreator( userInfo.getCreator() );
				obj.setCreateDate( userInfo.getCreateDate() );
			}
			obj.setModifiedDate( new Date() );
			obj.setDeleted( false );
			obj.setInfoText( infoText.getValue().toString() );
			obj.setFileName( fileName.getValue().toString() );

			app.getServiceUserInformation().saveOrUpdateUserInformation( obj );

			app.showConfigPage( true );
		}
		else
		{
			event.getButton().getApplication().getMainWindow().showNotification(	app.msg( "save.error" ),
																					Notification.TYPE_ERROR_MESSAGE );
		}
	}

	/**
	 * Action event for cancel button
	 * 
	 * @param event
	 */
	public void btnCancelClick( final ClickEvent event )
	{
		app.showConfigPage( false );
	}

}
