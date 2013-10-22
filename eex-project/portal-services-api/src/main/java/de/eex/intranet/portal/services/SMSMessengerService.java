package de.eex.intranet.portal.services;

import de.eex.intranet.portal.model.SMSSendResultState;
import de.eex.intranet.portal.model.Sms;

/**
 * User: mheineck Date: 30.09.13 Time: 12:06
 */
public interface SMSMessengerService
{
	public SMSSendResultState sendSMSToProvider( Sms sms ) throws SmsServiceException;
}
