package de.eex.intranet.portal.services;

import java.util.List;

import de.eex.intranet.portal.model.Shoutbox;

public interface ShoutboxService
{
	public int insertShoutbox( Shoutbox sb );

	public List<Shoutbox> getLastMessagesLimited( int limit );

	public int updateDelete( int id );

	public int deteleMesg( int id );

	public List<Shoutbox> getAllMesg();
}
