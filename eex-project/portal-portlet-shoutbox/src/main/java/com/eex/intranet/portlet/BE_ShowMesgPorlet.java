package com.eex.intranet.portlet;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.eex.intranet.portal.model.Shoutbox;
import de.eex.intranet.portal.services.ShoutboxService;

@Configurable(preConstruction =true)
public class BE_ShowMesgPorlet extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8711540051445494784L;
	
	private Window mainWindow = new Window();
	private Table tableMesg;
	private VerticalLayout mainContent;
	private final String TIME ="Uhrzeit";
	private final String  EDITOR = "Sender";
	private final String MESSAGE = "Nachricht";
	private final String ACTION = "action";
	
	@Autowired
	private ShoutboxService shoutboxService;
	
	@Override
	public void init() {
		mainWindow.setImmediate(true);
		setMainWindow(mainWindow);		
		
		createTable();
		mainWindow.addComponent(mainContent);
	}
	private final String[] visibleColumn = new String[] {TIME, EDITOR, MESSAGE, ACTION};
	private final String[] columnHeaders = new String[] {TIME, EDITOR, MESSAGE, ""};
	private void createTable() {
		mainContent = new VerticalLayout();
		tableMesg = new Table();
		tableMesg.setWidth("800px");
		tableMesg.addStyleName("shoutbox_tableMesg");
		tableMesg.setImmediate(true);
		tableMesg.setColumnCollapsingAllowed(true);
		tableMesg.setColumnReorderingAllowed(true);
		
		tableMesg.setContainerDataSource(getDataSource());
		
		tableMesg.setVisibleColumns(visibleColumn);
		tableMesg.setColumnHeaders(columnHeaders);
		
		tableMesg.setColumnWidth(ACTION,30);
		tableMesg.setColumnWidth(TIME, 40);
		tableMesg.setColumnWidth(MESSAGE, 600);
		tableMesg.setColumnAlignment(TIME, Table.ALIGN_CENTER);
		tableMesg.setSelectable(true);
		
		createGenColumn();
		
		mainContent.removeAllComponents();
		mainContent.addComponent(tableMesg);
		
	}	
	private IndexedContainer getDataSource() {
		final IndexedContainer c = new IndexedContainer();
		c.addContainerProperty( TIME, String.class, null );
		c.addContainerProperty( EDITOR, String.class, null );
		c.addContainerProperty( MESSAGE, String.class, null );
		c.addContainerProperty( ACTION, Shoutbox.class, null );
		

		final List<Shoutbox> lstMesg = shoutboxService.getAllMesg();
		if ( lstMesg == null )
		{
			return c;
		}
		int i = 0;
		for (Shoutbox shoutbox : lstMesg )
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
			Shoutbox s = shoutbox;
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
			item.getItemProperty(TIME).setValue(sdf.format( s.getDate()) );
			item.getItemProperty( EDITOR ).setValue( s.getName() );
			item.getItemProperty( MESSAGE ).setValue( s.getMesg() );			
			item.getItemProperty( ACTION ).setValue( s );
			i++;
		}
		return c;
		
	}
	private void createGenColumn() {
		tableMesg.addGeneratedColumn(ACTION, new ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				if(ACTION.equals(columnId)){
					final Item item = tableMesg.getItem(itemId);
					final Shoutbox sb = (Shoutbox)item.getItemProperty(ACTION).getValue();
					Button btnDelete = new NativeButton();
					btnDelete.setImmediate(true);
					btnDelete.setStyleName("shoutbox_admin_del");
					btnDelete.addListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							shoutboxService.updateDelete(sb.getId());
							tableMesg.setContainerDataSource(getDataSource());
							
						}
					});
					return btnDelete;
				}
				return null;
			}
		});
		
	}

}
