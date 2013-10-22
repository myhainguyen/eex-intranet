package de.eex.intranet.portal.dao;

import java.util.List;

import de.eex.intranet.portal.model.Shoutbox;

public interface ShoutboxDao
{
	public int insertShoutbox( Shoutbox sh );

	public List<Shoutbox> getLastMessagesLimited( int limit );

	public int updateDelete( int id );

	public int deteleMesg( int id );

	public List<Shoutbox> getAllMesg();
}
