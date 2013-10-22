package de.eex.intranet.portal.portlet.hotnews.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * Class to handle download event
 * 
 * @author hai.nguyen
 * 
 */
public class DownloadListener implements ClickListener
{
	private final String fileName;
	private final String filePath;

	/**
	 * Listener for download pdf file
	 * 
	 * @param fileName
	 *            name of selected file
	 * @param filePath
	 *            path of selected file
	 * 
	 */
	public DownloadListener( final String fileName, final String filePath )
	{
		this.fileName = fileName;
		this.filePath = filePath;
	}

	/**
	 * Click event, handle file download
	 * 
	 * @param event
	 *            {@link ClickEvent}
	 * 
	 */
	@Override
	public void buttonClick( final ClickEvent event )
	{
		final Window mainWindow = event.getButton().getApplication().getMainWindow();
		final File file = new File( filePath + File.separator + fileName );
		if ( !file.exists() )
		{
			mainWindow.showNotification( fileName + " nicht existiert.", Notification.TYPE_ERROR_MESSAGE );
		}
		else
		{
			final FileResource fileRes = new FileResource( file, event.getButton().getApplication() )
			{
				@Override
				public DownloadStream getStream()
				{
					try
					{
						final DownloadStream ds = new DownloadStream(	new FileInputStream( getSourceFile() ),
																		getMIMEType(),
																		getFilename() );
						ds.setParameter( "Content-Disposition", "attachment; filename=" + getFilename() );
						ds.setCacheTime( getCacheTime() );
						return ds;
					}
					catch ( final FileNotFoundException e )
					{
						return null;
					}
				}
			};
			mainWindow.open( fileRes );
		}
	}
}
