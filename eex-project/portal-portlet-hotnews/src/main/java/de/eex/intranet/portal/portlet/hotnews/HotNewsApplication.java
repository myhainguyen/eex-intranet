package de.eex.intranet.portal.portlet.hotnews;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2.PortletListener;
import com.vaadin.ui.Window;

import de.eex.intranet.portal.conf.ConfigurationKey;
import de.eex.intranet.portal.conf.PortalConfiguration;
import de.eex.intranet.portal.services.UserInformationService;

/**
 * Main application class for hotnews portlet
 * 
 * @author hai.nguyen
 * 
 */
@Configurable( preConstruction = true )
public class HotNewsApplication extends Application implements PortletListener
{

	private static final long serialVersionUID = -184491516638329151L;

	@Autowired
	private transient PortalConfiguration portalConfiguration;

	@Autowired
	private transient ResourceBundleMessageSource messageSource;

	@Autowired
	private transient UserInformationService userInformationService;

	private final Logger logger = LoggerFactory.getLogger( HotNewsApplication.class );

	private transient RenderRequest request;
	private ThemeDisplay themeDisplay;
	private LiferayPortletRequest liferayPortletRequest;
	private LiferayPortletResponse liferayPortletResponse;
	private MainPage mainPage;
	private ConfigPage configPage;
	private boolean refresh = false;

	/**
	 * Constructor
	 */
	public HotNewsApplication()
	{
		super();
	}

	/**
	 * Initializing hotnews portlet
	 */
	@Override
	public void init()
	{
		final Window mainWindow = new Window();
		mainWindow.addStyleName( "hotnews-portlet" );
		setMainWindow( mainWindow );
		setTheme( "eex" );
		if ( getContext() instanceof PortletApplicationContext2 )
		{
			final PortletApplicationContext2 ctx = (PortletApplicationContext2) getContext();

			// Add a custom listener to handle action and
			// render requests.
			ctx.addPortletListener( this, this );
		}
	}

	/* (non-Javadoc)
	 * @see com.vaadin.terminal.gwt.server.PortletApplicationContext2.PortletListener#handleRenderRequest(javax.portlet.RenderRequest, javax.portlet.RenderResponse, com.vaadin.ui.Window)
	 */
	@Override
	public void handleRenderRequest( final RenderRequest request, final RenderResponse response, final Window window )
	{
		this.request = request;
		themeDisplay = (ThemeDisplay) request.getAttribute( WebKeys.THEME_DISPLAY );
		this.request = request;
		// Edit mode
		if ( request.getPortletMode() == PortletMode.EDIT )
		{
			configPage = new ConfigPage( this );
			window.setContent( configPage );
		}
		// Initialize main content of hotnews portlet
		if ( request.getPortletMode() == PortletMode.VIEW )
		{
			mainPage = new MainPage( this );
			window.setContent( mainPage );

		}
	}

	/* (non-Javadoc)
	 * @see com.vaadin.terminal.gwt.server.PortletApplicationContext2.PortletListener#handleActionRequest(javax.portlet.ActionRequest, javax.portlet.ActionResponse, com.vaadin.ui.Window)
	 */
	@Override
	public void handleActionRequest( final ActionRequest request, final ActionResponse response, final Window window )
	{
	}

	/* (non-Javadoc)
	 * @see com.vaadin.terminal.gwt.server.PortletApplicationContext2.PortletListener#handleEventRequest(javax.portlet.EventRequest, javax.portlet.EventResponse, com.vaadin.ui.Window)
	 */
	@Override
	public void handleEventRequest( final EventRequest request, final EventResponse response, final Window window )
	{
	}

	/* (non-Javadoc)
	 * @see com.vaadin.terminal.gwt.server.PortletApplicationContext2.PortletListener#handleResourceRequest(javax.portlet.ResourceRequest, javax.portlet.ResourceResponse, com.vaadin.ui.Window)
	 */
	@Override
	public void handleResourceRequest(	final ResourceRequest request,
										final ResourceResponse response,
										final Window window )
	{
		if ( themeDisplay.isSignedIn() )
		{
			liferayPortletRequest = PortalUtil.getLiferayPortletRequest( request );
			liferayPortletResponse = PortalUtil.getLiferayPortletResponse( response );
		}

		if ( window != null )
		{
			// close calendar add event popup window
			window.executeJavaScript( "Liferay.fire('closeWindow',{id: '_8_addEvent'});" );

			// refresh mainPage
			if ( request.getPortletMode() == PortletMode.VIEW && isRefresh() )
			{
				mainPage = new MainPage( this );
				window.setContent( mainPage );

			}

			// execute bx-slider javascript
			if ( mainPage != null )
			{
				if ( mainPage.getArticlesCount() > 1 )
				{
					window.executeJavaScript( "$('.single-teaser.news ul.slider').bxSlider({controls: false,auto: true,autoControls: true,	mode: 'fade',touchEnabled: 0 });" );
					window.executeJavaScript( "$('.single-teaser.news ul.slider').removeClass('slider');" );
				}
				if ( mainPage.getInformationCount() > 1 )
				{
					window.executeJavaScript( "$('.single-teaser.information-panel ul.slider').bxSlider({controls: false,auto: true,autoControls: true,	mode: 'fade',touchEnabled: 0 });" );
					window.executeJavaScript( "$('.single-teaser.information-panel ul.slider').removeClass('slider');" );
				}
			}

		}

	}

	// getter
	public PortletPreferences getPortletPreferences()
	{
		return request.getPreferences();
	}

	public ThemeDisplay getThemeDisplay()
	{
		return themeDisplay;
	}

	public User getLiferayUser()
	{
		return themeDisplay.getUser();
	}

	public LiferayPortletRequest getLiferayPortletRequest()
	{
		return liferayPortletRequest;
	}

	public LiferayPortletResponse getLiferayPortletResponse()
	{
		return liferayPortletResponse;
	}

	public RenderRequest getRequest()
	{
		return request;
	}

	public UserInformationService getServiceUserInformation()
	{
		return userInformationService;
	}

	/**
	 * Get message from spring resource bundle
	 * 
	 * @param key
	 * @return String
	 */
	public String msg( final String key )
	{
		return messageSource.getMessage( key, null, null );
	}

	// get value from configuration file
	public String getConfigurationValue( final ConfigurationKey key )
	{
		return portalConfiguration.getConfigValue( key );
	}

	/**
	 * display main layout of config page
	 * 
	 * @param bRefresh
	 *            flag to determine if dataset need to be refreshed
	 */
	public void showConfigPage( final boolean bRefresh )
	{
		configPage.setMainLayout( bRefresh );
	}

	/**
	 * Flag to determine if the mainPage need to be refreshed
	 * 
	 * @param refresh
	 */
	public void setRefresh( final boolean refresh )
	{
		this.refresh = refresh;
	}

	public boolean isRefresh()
	{
		return refresh;
	}
}
