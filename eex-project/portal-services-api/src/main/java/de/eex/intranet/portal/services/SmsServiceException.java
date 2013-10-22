/*
 * Dies ist UNVERÖFFENTLICHTER URHEBERRECHTLICH GESCHÜTZTER QUELLCODE der EWERK
 * IT GmbH, der Inhalt dieser Datei darf ohne die ausdrückliche Erlaubnis der
 * EWERK IT GmbH nicht an Dritte weitergegeben, kopiert oder in sonstiger Form,
 * im Ganzen oder in Teilen weitergegeben werden. Eine Weitergabe an Dritte,
 * oder Veränderungen am Programm dürfen nur mit ausdrücklicher Genehmigung der
 * EWERK IT GmbH erfolgen. Copyright 2002-2011, EWERK IT GmbH, Alle Rechte
 * vorbehalten.
 *
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of EWERK IT GmbH, the contents of
 * this file may not be disclosed to third parties, copied or duplicated in any
 * form, in whole or in part, without the prior written permission of EWERK IT
 * GmbH. Distribution to a third party or changes at the data file are only
 * allowed with the explicit permission of EWERK IT GmbH. Copyright 2002-2011,
 * EWERK IT GmbH, All rights reserved
 */

package de.eex.intranet.portal.services;

/**
 * User: mheineck Date: 01.10.13 Time: 15:24
 */
public class SmsServiceException extends Exception
{
	public SmsServiceException()
	{
	}

	public SmsServiceException( final String s )
	{
		super( s );
	}

	public SmsServiceException( final String s, final Throwable throwable )
	{
		super( s, throwable );
	}

	public SmsServiceException( final Throwable throwable )
	{
		super( throwable );
	}
}
