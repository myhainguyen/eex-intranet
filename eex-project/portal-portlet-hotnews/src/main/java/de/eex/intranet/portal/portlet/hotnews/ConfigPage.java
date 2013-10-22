package de.eex.intranet.portal.portlet.hotnews;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

import de.eex.intranet.portal.model.UserInformation;
import de.eex.intranet.portal.portlet.hotnews.util.CustomTableFieldFactory;
import de.eex.intranet.portal.portlet.hotnews.util.PrefConstant;

/**
 * Config page for hot news portlet
 * 
 * @author hai.nguyen
 * 
 */

public class ConfigPage extends VerticalLayout
{

	private static final long serialVersionUID = 7425732614694046516L;

	private TextField backgroundUrl;
	private ComboBox vocabularySelect;
	private Table quicklinkTable;
	private Table informationTable;
	private transient PortletPreferences prefs;

	private Label label;
	private Label infoMsg;

	private int quicklinksCount;

	private final VerticalLayout layout;
	private final HotNewsApplication app;
	private final long groupId;

	private static final int IMAGE_WIDTH = 1165;
	private static final int IMAGE_HEIGHT = 340;

	private final Logger logger = LoggerFactory.getLogger( ConfigPage.class );

	private final User user;

	/**
	 * Constructor
	 * 
	 * @param app
	 *            {@link HotNewsApplication}
	 */
	public ConfigPage( final HotNewsApplication app )
	{
		this.app = app;
		this.prefs = app.getPortletPreferences();
		layout = new VerticalLayout();
		layout.setImmediate( true );
		layout.setWidth( 600, Sizeable.UNITS_PIXELS );
		groupId = app.getThemeDisplay().getScopeGroupId();
		user = app.getLiferayUser();
		initForm();
		addComponent( layout );
		setComponentAlignment( layout, Alignment.MIDDLE_CENTER );
	}

	/**
	 * Display mainLayout of config page
	 * 
	 * @param bRefresh
	 *            flag to refresh UserInformation dataset
	 */
	public void setMainLayout( final boolean bRefresh )
	{
		removeAllComponents();
		if ( bRefresh )
		{
			setData();
		}
		addComponent( layout );
	}

	/**
	 * Initializing form
	 */
	private void initForm()
	{
		final PermissionChecker permissionChecker = app.getThemeDisplay().getPermissionChecker();
		if ( permissionChecker.isOmniadmin() )
		{
			createBackgroundForm();
			createVocabularySelection();
			createQuicklinksTable();
		}
		final Button save = new Button( app.msg( "config.save.button" ) );
		save.addListener( new OnSaveClickListener() );
		layout.addComponent( save );

		createInformationTable();

		label = new Label();
		label.setImmediate( true );
		layout.addComponent( label );

	}

	/**
	 * Form for input background image url
	 */
	private void createBackgroundForm()
	{

		backgroundUrl = new TextField( app.msg( "hotnews.background.url" ) );
		backgroundUrl.setWidth( 100, Sizeable.UNITS_PERCENTAGE );
		backgroundUrl.setValue( prefs.getValue( PrefConstant.PREF_URL, "" ) );
		layout.addComponent( backgroundUrl );
	}

	/**
	 * Table for input quicklinks data
	 */
	private void createQuicklinksTable()
	{
		quicklinkTable = new Table();
		quicklinkTable.setWidth( 100, Sizeable.UNITS_PERCENTAGE );

		quicklinkTable.setSortDisabled( true );
		quicklinkTable.setCaption( app.msg( "hotnews.quicklink" ) );
		addTableContainerProperty( "Name", "name", "", quicklinkTable );
		addTableContainerProperty( "Friendly Url", "url", "", quicklinkTable );
		quicklinkTable.setTableFieldFactory( new CustomTableFieldFactory() );
		quicklinkTable.setEditable( true );

		final String[] quicklinks = prefs.getValues( PrefConstant.PREF_QUICKLINK, new String[] {} );
		final String[] quicklinksName = prefs.getValues( PrefConstant.PREF_QUICKLINK_NAME, new String[] {} );
		quicklinksCount = quicklinks.length;
		for ( int i = 0; i < ( quicklinksCount == 0 ? 1 : quicklinksCount ); i++ )
		{
			if ( quicklinksCount == 0 )
			{
				quicklinkTable.addItem();
				quicklinksCount++;
			}
			else
			{
				quicklinkTable.addItem( new Object[] { quicklinksName[i], quicklinks[i] }, i + 1 );
			}
		}

		quicklinkTable.setPageLength( 0 );
		layout.addComponent( quicklinkTable );

		final Button addRowButton = new Button( app.msg( "new.row" ) );
		addRowButton.addListener( new AddRowListener() );
		layout.addComponent( addRowButton );
	}

	/**
	 * Table for input data for information panel
	 */
	private void createInformationTable()
	{
		// Display info message if no data found
		infoMsg = new Label( app.msg( "information.table.empty" ) );
		infoMsg.addStyleName( "portlet-msg-info" );

		informationTable = new Table();
		informationTable.addStyleName( "information-table" );
		informationTable.setImmediate( true );
		informationTable.setWidth( 100, Sizeable.UNITS_PERCENTAGE );
		informationTable.setCaption( app.msg( "information.table" ) );

		informationTable.addContainerProperty( "info", Component.class, null );
		informationTable.addContainerProperty( "filename", Component.class, null );
		informationTable.addContainerProperty( "edit", Button.class, null );
		informationTable.addContainerProperty( "delete", Button.class, null );

		setData();

		informationTable.setColumnExpandRatio( "info", 1.0f );
		informationTable.setColumnExpandRatio( "filename", 1.0f );
		informationTable.setPageLength( 0 );

		final Button addBtn = new Button( app.msg( "add.button" ) );
		addBtn.addListener( ClickEvent.class, this, "addBtnClick" );

		layout.addComponent( infoMsg );
		layout.addComponent( informationTable );
		layout.addComponent( addBtn );
	}

	private Label createLabel( final String value )
	{
		final Label label = new Label( value );
		label.setWidth( 200, Sizeable.UNITS_PIXELS );
		return label;
	}

	/**
	 * Set data for information table
	 */
	public void setData()
	{
		informationTable.setVisible( true );
		infoMsg.setVisible( false );
		informationTable.removeAllItems();
		final List<UserInformation> lst = app.getServiceUserInformation().getUserInformationByCreator( user.getScreenName() );
		if ( lst.isEmpty() )
		{
			infoMsg.setVisible( true );
			informationTable.setVisible( false );
		}
		for ( final UserInformation obj : lst )
		{
			final Button editBtn = new Button( app.msg( "edit.button" ) );
			editBtn.addListener( ClickEvent.class, this, "editBtnClick" );
			editBtn.setData( obj );

			final Button deleteBtn = new Button( app.msg( "delete.button" ) );
			deleteBtn.addListener( ClickEvent.class, this, "deleteBtnClick" );
			deleteBtn.setData( obj );

			informationTable.addItem( new Object[] { createLabel( obj.getInfoText() ),
													createLabel( obj.getFileName() ),
													editBtn,
													deleteBtn }, obj.getId() );
		}
	}

	/**
	 * Action event for "Add" button
	 * 
	 * @param event
	 */
	public void addBtnClick( final ClickEvent event )
	{
		removeAllComponents();
		addComponent( new FrontEndEditView( app, null ) );
	}

	/**
	 * Action event for edit button
	 * 
	 * @param event
	 */
	public void editBtnClick( final ClickEvent event )
	{
		final UserInformation data = (UserInformation) event.getButton().getData();
		removeAllComponents();
		addComponent( new FrontEndEditView( app, data ) );
	}

	/**
	 * Action event for delete button
	 * 
	 * @param event
	 */
	public void deleteBtnClick( final ClickEvent event )
	{
		final UserInformation data = (UserInformation) event.getButton().getData();
		app.getServiceUserInformation().deleteUserInformation( data );
		informationTable.removeItem( data.getId() );

		if ( informationTable.size() == 0 )
		{
			informationTable.setVisible( false );
			infoMsg.setVisible( true );
		}
	}

	/**
	 * Drop down menu for selecting vocabulary
	 */
	private void createVocabularySelection()
	{
		vocabularySelect = new ComboBox( app.msg( "hotnews.vocabulary" ) );
		vocabularySelect.setImmediate( true );
		final DynamicQuery queryVocabularies = DynamicQueryFactoryUtil.forClass( AssetVocabulary.class );
		queryVocabularies.add( PropertyFactoryUtil.forName( "groupId" ).eq( groupId ) );
		try
		{
			final List<AssetVocabulary> vocabularies = AssetVocabularyLocalServiceUtil.dynamicQuery( queryVocabularies );
			for ( final AssetVocabulary vocabulary : vocabularies )
			{
				vocabularySelect.addItem( vocabulary.getVocabularyId() );
				vocabularySelect.setItemCaption( vocabulary.getVocabularyId(), vocabulary.getName().toLowerCase() );
			}
			layout.addComponent( vocabularySelect );
		}
		catch ( final SystemException e )
		{
			logger.debug( "An error has occured ", e );
		}
		vocabularySelect.setValue( Long.parseLong( prefs.getValue( PrefConstant.PREF_VOCABULARY, "0" ) ) );
	}

	/**
	 * Saving data to preferences
	 */
	private class OnSaveClickListener implements ClickListener
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8064247845323193453L;

		/* (non-Javadoc)
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick( final ClickEvent event )
		{
			try
			{
				// Save vocabulary
				prefs.setValue( PrefConstant.PREF_VOCABULARY,
								vocabularySelect.getValue() != null ? vocabularySelect.getValue().toString() : "0" );

				// Save quicklinks
				saveQuicklinks();

				// save background image
				if ( !backgroundUrl.getValue().toString().isEmpty() )
				{
					saveBackgroundImageURL();
				}
				else
				{
					prefs.store();
					label.setValue( app.msg( "pref.saved" ) );
					label.removeStyleName( "portlet-app.msg-error" );
					label.addStyleName( "portlet-app.msg-success" );
				}
			}
			catch ( final ReadOnlyException e )
			{
				initErrorMessage( app.msg( "save.failed" ) );
				logger.debug( "An error has occured: ", e.toString() );
			}
			catch ( final ValidatorException e )
			{
				initErrorMessage( app.msg( "save.failed" ) );
				logger.debug( "An error has occured: ", e.toString() );
			}
			catch ( final IOException e )
			{
				initErrorMessage( app.msg( "save.failed" ) );
				logger.debug( "An error has occured: ", e.toString() );
			}
		}
	}

	/**
	 * Save quicklinks table
	 * 
	 * @throws ReadOnlyException
	 */
	private void saveQuicklinks() throws ReadOnlyException
	{
		final List<String> quicklinks = new ArrayList<String>();
		final List<String> quicklinksName = new ArrayList<String>();
		for ( int i = 0; i < quicklinksCount; i++ )
		{
			final String quicklinksUrl = quicklinkTable.getContainerProperty( i + 1, "url" ).getValue().toString();
			final String quicklinksCaption = quicklinkTable.getContainerProperty( i + 1, "name" ).getValue().toString();
			if ( !quicklinksUrl.isEmpty() && !quicklinksCaption.isEmpty() )
			{
				quicklinks.add( quicklinksUrl );
				quicklinksName.add( quicklinksCaption );
			}

		}
		prefs.setValues( PrefConstant.PREF_QUICKLINK, quicklinks.toArray( new String[0] ) );
		prefs.setValues( PrefConstant.PREF_QUICKLINK_NAME, quicklinksName.toArray( new String[0] ) );
	}

	/**
	 * save background image
	 * 
	 * @throws ReadOnlyException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws ValidatorException
	 */
	private void saveBackgroundImageURL() throws ReadOnlyException, MalformedURLException, IOException, ValidatorException
	{
		final BufferedImage image = ImageIO.read( new URL( backgroundUrl.getValue().toString() ) );

		if ( image != null && image.getWidth() == IMAGE_WIDTH && image.getHeight() == IMAGE_HEIGHT )
		{
			// Save background image url
			prefs.setValue( PrefConstant.PREF_URL, backgroundUrl.getValue().toString() );

			prefs.store();
			label.setValue( app.msg( "pref.saved" ) );
			label.removeStyleName( "portlet-msg-error" );
			label.addStyleName( "portlet-msg-success" );
		}
		else
		{
			initErrorMessage( app.msg( "wrong.image.size" ) );
		}
	}

	/**
	 * ContainerProperty for {@link Table}
	 * 
	 * @param columnName
	 * @param id
	 * @param value
	 * @param table
	 */
	private void addTableContainerProperty( final String columnName,
											final String id,
											final String value,
											final Table table )
	{
		if ( value != null )
		{
			table.addContainerProperty( id, String.class, value );
		}
		table.setColumnHeader( id, columnName );
		table.setColumnExpandRatio( id, 1.0f );
	}

	/**
	 * Add new row to Quicklinks table
	 */
	private class AddRowListener implements Button.ClickListener
	{
		private static final long serialVersionUID = 6844832854578329254L;

		public AddRowListener()
		{
		}

		/* (non-Javadoc)
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick( final ClickEvent event )
		{
			if ( quicklinksCount < 6 )
			{
				quicklinkTable.addItem();
				quicklinksCount++;
			}
			else
			{
				app.getMainWindow().showNotification(	app.msg( "link.limit.reached" ),
														Notification.TYPE_WARNING_MESSAGE );
			}
		}
	}

	/**
	 * Create error message
	 * 
	 * @param message
	 *            : message to display
	 */
	private void initErrorMessage( final String message )
	{
		label.setValue( message );
		label.removeStyleName( "portlet-msg-success" );
		label.addStyleName( "portlet-msg-error" );
	}

}
