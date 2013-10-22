package com.eex.intranet.portlet.shoutbox.layout;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.eex.intranet.portlet.base.BaseView;
import com.liferay.portal.theme.ThemeDisplay;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.eex.intranet.portal.model.Shoutbox;
import de.eex.intranet.portal.services.ShoutboxService;

@Configurable( preConstruction = true )
public class ShoutboxViewLayout extends BaseView<VerticalLayout>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final VerticalLayout vrLayout = new VerticalLayout();
	private final VerticalLayout mainWindow = new VerticalLayout();
	private final CssLayout cssContent = new CssLayout();
	private final CssLayout cssSBSenden = new CssLayout();
	private Table shoutboxTable = new Table();
	private final String ITEM = "item";
	private final TextField txtMesg = new TextField();
	private final TextField txtName = new TextField();
	private Label lblMesg = new Label();
	private String name = "";
	private String screenName = "";
	private List<Shoutbox> lstMesg = new ArrayList<Shoutbox>();
	private int limit;

	private ThemeDisplay themeDisplay;

	public ThemeDisplay getThemeDisplay()
	{
		return themeDisplay;
	}

	public void setThemeDisplay( final ThemeDisplay themeDisplay )
	{
		this.themeDisplay = themeDisplay;
	}

	@Autowired
	private ShoutboxService shoutboxService;

	public ShoutboxViewLayout()
	{

		super( new VerticalLayout() );

		setContent( mainWindow );
		cssContent.removeAllComponents();
		mainWindow.setMargin( false );
		mainWindow.addComponent( cssContent );
	}

	public void createShoutbox()
	{
		cssContent.setImmediate( true );
		cssContent.setWidth( "310px" );
		cssContent.setStyleName( "shoutbox_cssContent" );
		shoutBoxSenden();
		cssContent.addComponent( cssSBSenden );
		shoutBoxContent( limit );

		cssContent.addComponent( vrLayout );

	}

	@SuppressWarnings( "serial" )
	private void shoutBoxSenden()
	{
		cssSBSenden.removeAllComponents();

		cssSBSenden.setWidth( "310px" );
		cssSBSenden.setStyleName( "shoutbox_cssSenden" );

		final Label lblShoutbox = new Label( "Shoutbox" );
		lblShoutbox.setStyleName( "shoutbox_lblShoutbox" );

		lblMesg = new Label( "Maximale Anzahl an Zeichern (140) wurde erreicht." );
		lblMesg.setStyleName( "shoutbox_lblMesg" );

		txtName.setInputPrompt( "Name eingeben" );
		txtName.setStyleName( "shoutbox_txtMesg" );
		txtName.setImmediate( true );
		txtName.setValue( name );

		txtMesg.setInputPrompt( "Text eingeben" );
		txtMesg.setStyleName( "shoutbox_txtMesg" );
		txtMesg.removeStyleName( "shoutbox_txtMesgEmpty" );
		txtMesg.setImmediate( true );
		//		txtMesg.setMaxLength(140);

		txtMesg.addShortcutListener( new ShortcutListener( "name", ShortcutAction.KeyCode.ENTER, null )
		{

			@Override
			public void handleAction( final Object sender, final Object target )
			{
				if ( target == txtMesg )
				{
					shoutboxAction( txtMesg.getValue() );
				}

			}
		} );

		final Button btnSenden = new Button( "Senden" );
		btnSenden.setImmediate( true );
		btnSenden.setStyleName( "shoutbox_btnSenden" );
		btnSenden.addListener( new ClickListener()
		{

			@Override
			public void buttonClick( final ClickEvent event )
			{
				shoutboxAction( txtMesg.getValue() );
			}
		} );

		cssSBSenden.addComponent( txtName );
		cssSBSenden.addComponent( txtMesg );

		cssSBSenden.addComponent( btnSenden );
		cssSBSenden.addComponent( lblMesg );
		cssContent.addComponent( lblShoutbox );

	}

	private void shoutboxAction( final Object obj )
	{
		final String mesg = obj.toString();
		if ( mesg.length() == 0 )
		{
			txtMesg.addStyleName( "shoutbox_txtMesgEmpty" );
			lblMesg.removeStyleName( "shoutbox_lblMesg_display" );
		}
		else if ( mesg.length() >= 140 )
		{
			lblMesg.addStyleName( "shoutbox_lblMesg_display" );
		}
		else
		{
			lblMesg.removeStyleName( "shoutbox_lblMesg_display" );
			txtMesg.removeStyleName( "shoutbox_txtMesgEmpty" );
			final Shoutbox sb = new Shoutbox();
			sb.setScreenName( getScreenName() );
			if ( name == "" && txtName.getValue().equals( "" ) )
			{
				sb.setName( "Anonym" );
				sb.setScreenName( null );

			}
			else if ( name != "" && txtName.getValue().equals( "" ) )
			{
				sb.setName( "Anonym" );
				sb.setScreenName( getScreenName() );
			}
			else
			{
				sb.setName( txtName.getValue().toString() );
				if ( name != "" && name != null )
				{
					name = sb.getName();
				}
				else
				{
					sb.setScreenName( null );
				}

			}
			sb.setDate( new Timestamp( new Date().getTime() ) );
			sb.setMesg( txtMesg.getValue().toString() );

			final int rs = shoutboxService.insertShoutbox( sb );
			if ( rs > 0 )
			{
				txtMesg.setValue( "" );
				txtName.setValue( name );
			}
			shoutBoxContent( limit );

		}
	}

	private void shoutBoxContent( final int limit )
	{

		shoutboxTable = new Table();
		shoutboxTable.removeAllItems();

		shoutboxTable.setWidth( "310px" );
		shoutboxTable.setStyleName( "shoutbox_table" );
		shoutboxTable.setColumnCollapsingAllowed( true );
		shoutboxTable.setColumnReorderingAllowed( true );
		shoutboxTable.setPageLength( 0 );
		shoutboxTable.setContainerDataSource( getDataSource( limit ) );
		shoutboxTable.setVisibleColumns( new String[] { ITEM } );
		shoutboxTable.setImmediate( true );
		createGenColumn();

		vrLayout.setStyleName( "shoutbox_vrlayout" );
		vrLayout.removeAllComponents();
		vrLayout.setWidth( "310px" );
		vrLayout.setImmediate( true );
		vrLayout.addComponent( shoutboxTable );

	}

	@SuppressWarnings( "serial" )
	private void createGenColumn()
	{
		shoutboxTable.addGeneratedColumn( ITEM, new ColumnGenerator()
		{

			@Override
			public Object generateCell( final Table source, final Object itemId, final Object columnId )
			{
				if ( ITEM.equals( columnId ) )
				{
					final Item itemdown = shoutboxTable.getItem( itemId );
					final Shoutbox data = (Shoutbox) itemdown.getItemProperty( ITEM ).getValue();
					final CssLayout cssItem = new CssLayout();

					final Button btnDel = new NativeButton();
					btnDel.setStyleName( "shoutbox_del" );
					btnDel.setImmediate( true );

					btnDel.addListener( new ClickListener()
					{

						@Override
						public void buttonClick( final ClickEvent event )
						{
							shoutboxService.updateDelete( data.getId() );
							shoutBoxContent( limit );
						}
					} );

					cssItem.setWidth( "277px" );
					cssItem.setStyleName( "shoutbox_cssItem" );
					final SimpleDateFormat sdf = new SimpleDateFormat( "dd.MM" );

					final Label lblitem = new Label(	"<p>" + sdf.format( data.getDate() ) + "</p>. " + data.getName(),
							Label.CONTENT_XHTML );
					lblitem.setStyleName( "shoutbox_name" );
					final Label lblItemContent = new Label( data.getMesg() );
					lblItemContent.setStyleName( "shoutbox_itemContent" );
					//administrator can delete the messages in the fontend
					if ( themeDisplay.isSignedIn() && themeDisplay.getPermissionChecker().isOmniadmin() )
					{
						cssItem.addComponent( btnDel );
					}
					else if ( screenName.equals( data.getScreenName() ) && data.getScreenName() != null )
					{

						cssItem.addComponent( btnDel );
					}

					cssItem.addComponent( lblitem );
					cssItem.addComponent( lblItemContent );
					return cssItem;
				}
				return null;
			}
		} );

	}

	private Container getDataSource( final int limit )
	{
		final IndexedContainer c = new IndexedContainer();
		c.addContainerProperty( ITEM, Shoutbox.class, null );

		lstMesg = shoutboxService.getLastMessagesLimited( limit );
		System.out.println( "lst size: " + lstMesg.size() );

		if ( lstMesg == null )
		{
			return c;
		}
		int i = 0;
		for ( final Shoutbox s : lstMesg )
		{
			Item item;
			if ( c.getItem( i ) == null )
			{
				item = c.addItem( i );
			}
			else
			{
				item = c.addItem( i );
			}
			item.getItemProperty( ITEM ).setValue( s );
			i++;
		}
		return c;
	}

	public String getName()
	{
		return name;
	}

	public void setName( final String name )
	{
		this.name = name;
	}

	public int getLimit()
	{
		return limit;
	}

	public void setLimit( final int limit )
	{
		this.limit = limit;
	}

	public String getScreenName()
	{
		return screenName;
	}

	public void setScreenName( final String screenName )
	{
		this.screenName = screenName;
	}

}
