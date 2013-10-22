package de.eex.intranet.portal.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.eex.intranet.portal.dao.ShoutboxDao;
import de.eex.intranet.portal.model.Shoutbox;

@Service( "shoutboxService" )
public class ShoutBoxServiceImpl implements ShoutboxService
{

	@Autowired
	private ShoutboxDao shoutboxDao;

	@Override
	@Transactional
	public int insertShoutbox( final Shoutbox sb )
	{
		return shoutboxDao.insertShoutbox( sb );
	}

	@Override
	@Transactional
	public List<Shoutbox> getLastMessagesLimited( final int limit )
	{
		return shoutboxDao.getLastMessagesLimited( limit );
	}

	@Override
	@Transactional
	public int updateDelete( final int id )
	{
		// TODO Auto-generated method stub
		return shoutboxDao.updateDelete( id );
	}

	@Override
	@Transactional
	public int deteleMesg( final int id )
	{
		// TODO Auto-generated method stub
		return shoutboxDao.deteleMesg( id );
	}

	@Override
	@Transactional
	public List<Shoutbox> getAllMesg()
	{
		// TODO Auto-generated method stub
		return shoutboxDao.getAllMesg();
	}

}
