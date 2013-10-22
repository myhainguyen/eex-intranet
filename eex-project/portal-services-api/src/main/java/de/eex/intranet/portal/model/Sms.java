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

package de.eex.intranet.portal.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User: mheineck Date: 30.09.13 Time: 01:15
 */
public class Sms implements Serializable
{
	private static final long serialVersionUID = -1L;
	@NotNull
	private String areaCode;
	@NotNull
	private String phone;
	@NotNull
	@Size( max = 130 )
	private String smsText;

	public Sms()
	{
		this.areaCode = "";
		this.phone = "";
		this.smsText = "";
	}

	public String getSmsText()
	{
		return smsText;
	}

	public void setSmsText( final String smsText )
	{
		this.smsText = smsText;
	}

	public String getAreaCode()
	{
		return areaCode;
	}

	public void setAreaCode( final String areaCode )
	{
		this.areaCode = areaCode;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone( final String phone )
	{
		this.phone = phone;
	}

	public String getCompletePhoneNumber( char delemiter )
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append( getAreaCode() );
		stringBuilder.append( delemiter );
		stringBuilder.append( getPhone() );
		return stringBuilder.toString();
	}
}
