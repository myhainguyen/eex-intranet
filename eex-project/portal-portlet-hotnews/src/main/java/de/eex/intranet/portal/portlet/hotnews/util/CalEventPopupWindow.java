package de.eex.intranet.portal.portlet.hotnews.util;

import java.util.Calendar;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.calendar.model.CalEvent;
import com.liferay.portlet.calendar.service.CalEventLocalServiceUtil;
import com.liferay.portlet.calendar.service.CalEventServiceUtil;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

import de.eex.intranet.portal.portlet.hotnews.HotNewsApplication;
import de.eex.intranet.portal.services.UserInformationService;

/**
 * Layout for calendar event popup window
 * 
 * @author hai.nguyen
 * 
 */
public class CalEventPopupWindow extends Window
{

	@Autowired
	private transient UserInformationService userInformationService;
	private final HotNewsApplication app;
	private final Calendar calendar;
	private Table table;
	private final Logger logger = LoggerFactory.getLogger( CalEventPopupWindow.class );

	/**
	 * Constructor
	 * 
	 * @param app
	 *            {@link HotNewsApplication}
	 * @param calendar
	 *            selected calendar date
	 */
	public CalEventPopupWindow( final HotNewsApplication app, final Calendar calendar )
	{
		this.app = app;
		this.calendar = calendar;
		setCaption( app.msg( "calevent" ) );
		setHeight( null );
		setWidth( 600, Sizeable.UNITS_PIXELS );
		setScrollable( true );
		center();

		initLayout();
	}

	/**
	 * Create layout to display list of CalEvent object
	 */
	public void initLayout()
	{
		final CssLayout layout = new CssLayout();
		layout.setMargin( false );
		layout.addStyleName( "calevent-popup" );

		final Label modalityCurtain = new Label(	"<div class='v-window-modalitycurtain' style='z-index:-1;'/>",
													Label.CONTENT_XHTML );
		layout.addComponent( modalityCurtain );
		try
		{
			final List<CalEvent> lst = CalEventServiceUtil.getEvents(	app.getThemeDisplay().getScopeGroupId(),
																		calendar,
																		"" );
			if ( !lst.isEmpty() )
			{
				table = new Table();
				table.addContainerProperty( "eventName", String.class, "" );
				table.addContainerProperty( "eventDesc", String.class, "" );
				table.addContainerProperty( "edit", Button.class, null );
				table.addContainerProperty( "delete", Button.class, null );

				table.setColumnHeaders( new String[] {	app.msg( "calevent.name" ),
														app.msg( "calevent.description" ),
														"",
														"" } );
				table.setColumnExpandRatio( "eventName", 0.5f );
				table.setColumnExpandRatio( "eventDesc", 1f );

				if ( lst.size() < 20 )
				{
					table.setPageLength( 0 );
				}
				else
				{
					table.setPageLength( 20 );
				}
				table.setWidth( 100, Sizeable.UNITS_PERCENTAGE );
				for ( final CalEvent event : lst )
				{
					final Button editBtn = createActionButton( app.msg( "edit.button" ), "editCalEvent", event );
					final Button delBtn = createActionButton( app.msg( "delete.button" ), "deleteCalEvent", event );

					table.addItem( new Object[] {	event.getTitle(),
													stripHtml( event.getDescription() ),
													editBtn,
													delBtn }, event.getEventId() );
				}
				layout.addComponent( table );
			}
			else
			{
				final Label label = new Label( app.msg( "calevent.not.exist" ) );
				label.addStyleName( "calevent-msg" );
				layout.addComponent( label );
			}
			final Button addEventBtn = new Button( app.msg( "calevent.add" ) );
			addEventBtn.addListener( ClickEvent.class, this, "addCalendarEvent" );

			layout.addComponent( addEventBtn );
		}
		catch ( final PortalException e )
		{
			logger.debug( "An error has occured: ", e );
		}
		catch ( final SystemException e )
		{
			logger.debug( "An error has occured: ", e );
		}

		setContent( layout );
	}

	/**
	 * Action event for add new calendar event
	 * 
	 * @param event
	 */
	public void addCalendarEvent( final ClickEvent event )
	{
		app.getMainWindow().executeJavaScript( "location.href=\"" + getPopupURL( null ) + "\";" );
		app.getMainWindow().removeWindow( CalEventPopupWindow.this );
		app.setRefresh( true );
	}

	/**
	 * Action event for edit Calendar event button
	 * 
	 * @param event
	 */
	public void editCalEvent( final ClickEvent event )
	{
		final CalEvent calEvent = (CalEvent) event.getButton().getData();
		app.getMainWindow().executeJavaScript( "location.href=\"" + getPopupURL( String.valueOf( calEvent.getEventId() ) ) + "\";" );
		app.getMainWindow().removeWindow( CalEventPopupWindow.this );
		app.setRefresh( true );
	}

	/**
	 * Action event for delete Calendar event button
	 * 
	 * @param event
	 */
	public void deleteCalEvent( final ClickEvent event )
	{

		try
		{
			// remove selected calendar event
			final CalEvent data = (CalEvent) event.getButton().getData();
			CalEventLocalServiceUtil.deleteEvent( data );
			table.removeItem( data.getEventId() );
		}
		catch ( final PortalException e )
		{
			logger.debug( "An error has occured: ", e );
		}
		catch ( final SystemException e )
		{
			logger.debug( "An error has occured: ", e );
		}

	}

	private Button createActionButton( final String caption, final String eventListener, final Object data )
	{
		final Button button = new Button( caption );
		button.setData( data );
		button.addListener( ClickEvent.class, this, eventListener );
		return button;
	}

	/**
	 * Get URL for new calendar event popup
	 * 
	 * @return String
	 */
	private String getPopupURL( final String eventId )
	{
		final LiferayPortletRequest liferayPortletRequest = app.getLiferayPortletRequest();
		final LiferayPortletResponse liferayPortletResponse = app.getLiferayPortletResponse();

		final String currentURL = app.getThemeDisplay().getURLCurrent();
		final AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName( CalEvent.class.getName() );
		liferayPortletRequest.setAttribute( WebKeys.ASSET_RENDERER_FACTORY_CLASS_TYPE_ID, 0 );

		PortletURL addPortletURL;
		try
		{
			addPortletURL = assetRendererFactory.getURLAdd( liferayPortletRequest, liferayPortletResponse );

			addPortletURL.setWindowState( LiferayWindowState.POP_UP );
			addPortletURL.setPortletMode( PortletMode.VIEW );
			addPortletURL.setParameter( "redirect", currentURL );
			final String referringPortletResource = ParamUtil.getString( liferayPortletRequest, "portletResource" );

			addPortletURL.setParameter( "referringPortletResource", referringPortletResource );
			if ( eventId == null )
			{
				addPortletURL.setParameter( "month", String.valueOf( calendar.get( Calendar.MONTH ) ) );
				addPortletURL.setParameter( "day", String.valueOf( calendar.get( Calendar.DATE ) ) );
				addPortletURL.setParameter( "year", String.valueOf( calendar.get( Calendar.YEAR ) ) );
			}
			else
			{
				addPortletURL.setParameter( "eventId", eventId );
			}
			return "javascript:Liferay.Util.openWindow({dialog: {width: 960}, id: '_8_addEvent', title: 'Calendar Event', uri:'" + HtmlUtil.escapeURL( addPortletURL.toString() ) + "'});";
		}
		catch ( final WindowStateException e )
		{
			logger.debug( "An error has occured: ", e );
		}
		catch ( final PortletModeException e )
		{
			logger.debug( "An error has occured: ", e );
		}
		catch ( final PortalException e )
		{
			logger.debug( "An error has occured: ", e );
		}
		catch ( final SystemException e )
		{
			logger.debug( "An error has occured: ", e );
		}
		return "";
	}

	/**
	 * Remove all html tag from string
	 * 
	 * @param s
	 * @return
	 */
	private String stripHtml( final String s )
	{
		return s.replaceAll( "\\<.*?>", "" );
	}
}