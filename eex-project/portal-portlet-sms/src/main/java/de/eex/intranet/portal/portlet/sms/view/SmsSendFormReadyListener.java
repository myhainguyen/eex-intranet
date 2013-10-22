package de.eex.intranet.portal.portlet.sms.view;

import de.eex.intranet.portal.model.Sms;

/**
 * User: mheineck Date: 30.09.13 Time: 12:26
 */
public interface SmsSendFormReadyListener
{
	public void onCommitSMS( Sms sms );

	//used for invalid data callback
	public void onInvalid( Sms sms );
}
