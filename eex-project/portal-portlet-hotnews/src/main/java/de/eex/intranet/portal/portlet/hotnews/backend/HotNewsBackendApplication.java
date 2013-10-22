package de.eex.intranet.portal.portlet.hotnews.backend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.gwt.server.PortletRequestListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

import de.eex.intranet.portal.conf.ConfigurationKey;
import de.eex.intranet.portal.conf.PortalConfiguration;
import de.eex.intranet.portal.model.UserInformation;
import de.eex.intranet.portal.services.UserInformationService;

/**
 * Backend application class for hotnews portlet
 * 
 * @author hai.nguyen
 * 
 */
@Configurable( preConstruction = true )
public class HotNewsBackendApplication extends Application implements PortletRequestListener
{

	private CssLayout mainLayout;
	private ThemeDisplay themeDisplay;
	private IndexedContainer container;
	private Window mainWindow;
	private Logger logger = LoggerFactory.getLogger( HotNewsBackendApplication.class );
	private Label infoMsg;
	private Table userInfoTable;
	@Autowired
	private transient PortalConfiguration portalConfiguration;

	@Autowired
	private transient ResourceBundleMessageSource messageSource;

	@Autowired
	private transient UserInformationService userInformationService;

	/* (non-Javadoc)
	 * @see com.vaadin.Application#init()
	 */
	@Override
	public void init()
	{
		mainWindow = new Window();
		mainWindow.addStyleName( "hotnews-backend" );
		setMainWindow( mainWindow );

		mainLayout = new CssLayout();
		mainLayout.setWidth( 100, Sizeable.UNITS_PERCENTAGE );
		mainWindow.setContent( mainLayout );

		initContent();
	}

	/**
	 * Initialize backend content
	 */
	public void initContent()
	{
		// Display info message if no data found
		infoMsg = new Label( msg( "information.table.empty" ) );
		infoMsg.addStyleName( "portlet-msg-info" );
		mainLayout.addComponent( infoMsg );

		// Display table container for data
		userInfoTable = new CustomTable();
		userInfoTable.setImmediate( true );
		userInfoTable.setWidth( 100, Sizeable.UNITS_PERCENTAGE );

		container = new IndexedContainer();
		container.addContainerProperty( "creator", Component.class, "" );
		container.addContainerProperty( "description", Component.class, "" );
		container.addContainerProperty( "filename", Component.class, "" );
		container.addContainerProperty( "createDate", Date.class, "" );
		container.addContainerProperty( "modifiedDate", Date.class, "" );
		container.addContainerProperty( "edit", Button.class, null );
		container.addContainerProperty( "delete", Button.class, null );

		setDataSource();

		userInfoTable.setContainerDataSource( container );
		userInfoTable.setColumnHeaders( new String[] {	msg( "information.createdBy" ),
														msg( "information.text" ),
														msg( "information.filename" ),
														msg( "information.createdDate" ),
														msg( "information.modifiedDate" ),
														"",
														"" } );

		userInfoTable.setColumnExpandRatio( "creator", 0.7f );
		userInfoTable.setColumnExpandRatio( "description", 1.2f );
		userInfoTable.setColumnExpandRatio( "filename", 0.7f );
		userInfoTable.setColumnExpandRatio( "createDate", 0.7f );
		userInfoTable.setColumnExpandRatio( "modifiedDate", 0.7f );
		if ( userInfoTable.size() < 25 )
		{
			userInfoTable.setPageLength( 0 );
		}
		else
		{
			userInfoTable.setPageLength( 25 );
		}
		mainLayout.addComponent( userInfoTable );

		// add button
		Button addBtn = new Button( msg( "add.button" ) );
		addBtn.addListener( ClickEvent.class, this, "addBtnClick" );
		mainLayout.addComponent( addBtn );
	}

	/**
	 * set data for table view
	 */
	public void setDataSource()
	{
		infoMsg.setVisible( false );
		userInfoTable.setVisible( true );
		SimpleDateFormat sdf = new SimpleDateFormat( "dd.MM.yyyy hh:mm:ss" );
		final List<UserInformation> lst = userInformationService.getUserInformations( 0 );
		if ( lst.isEmpty() )
		{
			// show info message and disable table view
			infoMsg.setVisible( true );
			userInfoTable.setVisible( false );
		}
		else
		{
			container.removeAllItems();
			for ( UserInformation userInfo : lst )
			{
				Item item = container.addItem( userInfo.getId() );

				item.getItemProperty( "creator" ).setValue( createLabel( userInfo.getCreator() ) );
				item.getItemProperty( "description" ).setValue( createLabel( userInfo.getInfoText() ) );
				item.getItemProperty( "filename" ).setValue( createLabel( userInfo.getFileName() ) );
				item.getItemProperty( "createDate" ).setValue( userInfo.getCreateDate() );
				item.getItemProperty( "modifiedDate" ).setValue( userInfo.getModifiedDate() );

				item.getItemProperty( "edit" ).setValue( createActionButton(	msg( "edit.button" ),
																				"editBtnClick",
																				userInfo ) );
				item.getItemProperty( "delete" ).setValue( createActionButton(	msg( "delete.button" ),
																				"deleteBtnClick",
																				userInfo ) );
			}
		}
	}

	/**
	 * Action event for add button
	 * 
	 * @param event
	 */
	public void addBtnClick( final ClickEvent event )
	{
		mainWindow.setContent( new BackendEditView( this, null ) );
	}

	/**
	 * Action event for edit button
	 * 
	 * @param event
	 */
	public void editBtnClick( final ClickEvent event )
	{
		final de.eex.intranet.portal.model.UserInformation data = (UserInformation) event.getButton().getData();
		mainWindow.setContent( new BackendEditView( this, data ) );

	}

	/**
	 * Action event for delete button
	 * 
	 * @param event
	 */
	public void deleteBtnClick( final ClickEvent event )
	{
		final UserInformation data = (UserInformation) event.getButton().getData();
		userInformationService.deleteUserInformation( data );
		container.removeItem( data.getId() );

		if ( userInfoTable.size() == 0 )
		{
			infoMsg.setVisible( true );
			userInfoTable.setVisible( false );
		}
	}

	/**
	 * Render action button
	 * 
	 * @param caption
	 *            button description
	 * @param eventListener
	 *            listener to be attached to the button
	 * @param data
	 *            {@link UserInformation} to bind to button
	 * @return {@link Button}
	 */
	private Button createActionButton( final String caption, final String eventListener, final UserInformation data )
	{
		Button btn = new Button( caption );
		btn.addListener( ClickEvent.class, this, eventListener );
		btn.setData( data );

		return btn;
	}

	/**
	 * Get message from spring resource bundle
	 * 
	 * @param key
	 * @return
	 */
	public String msg( final String key )
	{
		return messageSource.getMessage( key, null, null );
	}

	/**
	 * Get configuration value
	 * 
	 * @param key
	 * @return
	 */
	public String getConfigurationValue( final ConfigurationKey key )
	{
		return portalConfiguration.getConfigValue( key );
	}

	// getter
	public ThemeDisplay getThemeDisplay()
	{
		return themeDisplay;
	}

	public CssLayout getMainLayout()
	{
		return mainLayout;
	}

	public UserInformationService getServiceUserInformation()
	{
		return userInformationService;
	}

	@Override
	public void onRequestStart( PortletRequest request, PortletResponse response )
	{
		themeDisplay = (ThemeDisplay) request.getAttribute( WebKeys.THEME_DISPLAY );
		if ( container != null )
		{
			setDataSource();
		}
	}

	@Override
	public void onRequestEnd( PortletRequest request, PortletResponse response )
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Custom table class to display date in right format
	 * 
	 * @author hai.nguyen
	 * 
	 */
	private class CustomTable extends Table
	{
		private static final long serialVersionUID = -8550387803092106946L;

		/**
		 * Display style for Date field
		 */
		@Override
		protected String formatPropertyValue( final Object rowId, final Object colId, final Property property )
		{
			final Object o = property.getValue();
			if ( o instanceof Date )
			{
				final SimpleDateFormat sdf = new SimpleDateFormat( "dd.MM.yyyy hh:mm:ss" );
				return sdf.format( (Date) o );
			}
			return super.formatPropertyValue( rowId, colId, property );
		}
	}

	private Label createLabel( final String value )
	{
		final Label label = new Label( value );
		label.setWidth( 100, Sizeable.UNITS_PIXELS );
		return label;
	}

}
