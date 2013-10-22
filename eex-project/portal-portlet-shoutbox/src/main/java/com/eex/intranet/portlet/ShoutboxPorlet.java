package com.eex.intranet.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.eex.intranet.portlet.helper.ApplicationConstant;
import com.eex.intranet.portlet.shoutbox.layout.ShoutboxEditLayout;
import com.eex.intranet.portlet.shoutbox.layout.ShoutboxViewLayout;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2.PortletListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.eex.intranet.portal.services.ShoutboxService;

@Configurable( preConstruction = true )
public class ShoutboxPorlet extends Application implements PortletListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Window mainWindow = new Window();
	private User user;
	private String name = "";
	private ThemeDisplay themeDisplay;
	private ShoutboxViewLayout viewLayout;
	private ShoutboxEditLayout editLayout;
	private int limit;
	@Autowired
	private ShoutboxService shoutboxService;

	@Override
	public void init()
	{
		mainWindow.setImmediate( true );
		setMainWindow( mainWindow );

		if ( getContext() instanceof PortletApplicationContext2 )
		{
			final PortletApplicationContext2 ctx = (PortletApplicationContext2) getContext();
			ctx.addPortletListener( this, this );

		}
		else
		{
			mainWindow.showNotification( "not running in portal", Notification.TYPE_ERROR_MESSAGE );
		}

	}

	@Override
	public void handleRenderRequest( final RenderRequest request, final RenderResponse response, final Window window )
	{
		themeDisplay = (ThemeDisplay) request.getAttribute( WebKeys.THEME_DISPLAY );
		final String l = request.getPreferences().getValue( ApplicationConstant.COUNT_MESG, "3" );
		String screenName = "";
		if ( themeDisplay.isSignedIn() )
		{
			user = themeDisplay.getUser();
			name = user.getFirstName() + " " + user.getLastName();
			screenName = user.getScreenName();
		}

		limit = Integer.parseInt( l );
		mainWindow.removeAllComponents();
		if ( request.getPortletMode() == PortletMode.VIEW )
		{
			viewLayout = new ShoutboxViewLayout();
			viewLayout.setLimit( limit );
			viewLayout.setName( name );
			viewLayout.setScreenName( screenName );
			viewLayout.setThemeDisplay( themeDisplay );
			viewLayout.createShoutbox();
			mainWindow.setContent( viewLayout.getContent() );
		}
		if ( request.getPortletMode() == PortletMode.EDIT )
		{
			editLayout = new ShoutboxEditLayout( request.getPreferences() );
			editLayout.setLimit( limit );
			// editLayout.setUserID(user.getUserId());
			editLayout.createLayout();
			mainWindow.setContent( editLayout.getContent() );
		}

	}

	@Override
	public void handleActionRequest( final ActionRequest request, final ActionResponse response, final Window window )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void handleEventRequest( final EventRequest request, final EventResponse response, final Window window )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void handleResourceRequest(	final ResourceRequest request,
			final ResourceResponse response,
			final Window window )
	{
		// TODO Auto-generated method stub

	}

}
