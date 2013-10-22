package de.eex.intranet.portal.portlet.hotnews.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.vaadin.ui.Label;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

/**
 * Class to handle file upload
 * 
 * @author hai.nguyen
 * 
 */
public class UploadListener implements Receiver, SucceededListener
{

	private String filename;
	private final Label label;
	private final String uploadLocation;

	/**
	 * 
	 * @param label
	 *            {@link Label} to display name of uploaded file
	 * @param uploadLocation
	 *            location to store uploaded file
	 */
	public UploadListener( final Label label, final String uploadLocation )
	{
		this.label = label;
		this.uploadLocation = uploadLocation;
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Upload.SucceededListener#uploadSucceeded(com.vaadin.ui.Upload.SucceededEvent)
	 */
	@Override
	public void uploadSucceeded( final SucceededEvent event )
	{
		label.setValue( filename );
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Upload.Receiver#receiveUpload(java.lang.String, java.lang.String)
	 */
	@Override
	public OutputStream receiveUpload( final String filename, final String mimeType )
	{
		FileOutputStream fos = null;
		try
		{
			this.filename = filename;
			final File location = new File( uploadLocation );
			if ( !location.exists() )
			{
				location.mkdirs();
			}
			final File file = new File( location, filename );
			fos = new FileOutputStream( file );
		}
		catch ( final FileNotFoundException e )
		{
			return null;
		}
		return fos;
	}

}