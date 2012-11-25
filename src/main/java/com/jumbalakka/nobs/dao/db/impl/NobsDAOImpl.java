package com.jumbalakka.nobs.dao.db.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.jumbalakka.commons.config.JumbalakkaPropertyPlaceHolderConfigure;
import com.jumbalakka.commons.config.dao.TupleDAO;
import com.jumbalakka.commons.config.type.Tuple;
import com.jumbalakka.commons.security.JumbalakkaSecurityUtils;
import com.jumbalakka.nobs.dao.NobsDAO;
import com.jumbalakka.nobs.exception.NobsException;
import com.jumbalakka.nobs.type.NobsAuditLog;
import com.jumbalakka.nobs.type.NobsAuditLog.LOGTYPE;
import com.jumbalakka.nobs.type.NobsBillHeader;
import com.jumbalakka.nobs.type.NobsBillLine;
import com.jumbalakka.nobs.type.NobsLinePayers;
import com.jumbalakka.nobs.type.NobsUser;
import com.jumbalakka.nobs.type.NobsUser.USERSTATUS;
import com.jumbalakka.nobs.type.NobsUsersGroup;

public class NobsDAOImpl extends HibernateDaoSupport implements NobsDAO
{
	TupleDAO tupleDAO;
	
	
	public void setTupleDAO( TupleDAO tupleDAO )
	{
		this.tupleDAO = tupleDAO;
	}

	/* (non-Javadoc)
	 * @see com.jumbalakka.nobs.dao.db.impl.NobsDAO#appOnceInit()
	 */
	public void appOnceInit() throws NobsException
	{
		if( StringUtils.equals( 
				JumbalakkaPropertyPlaceHolderConfigure.
					getPropertyValue( "onetime.admin.init" ), "true" ) )
		{
			try
			{
				appOnceInitAdminUserCreate();
			}
			catch( Exception e )
			{
				throw new NobsException( e );
			}
		}
		
		//onetime.default.init
		if( StringUtils.equals( 
				JumbalakkaPropertyPlaceHolderConfigure.
					getPropertyValue( "onetime.default.init" ), "true" ) )
		{
			try
			{
				appOnceInitUserCreate();
			}
			catch( Exception e )
			{
				throw new NobsException( e );
			}
		}
		
		//create config key for each user id
		if( StringUtils.equals( 
				JumbalakkaPropertyPlaceHolderConfigure.
					getPropertyValue( "onetime.default.addfriend.init" ), "true" ) )
		{
			try
			{
				oneTimeaddFriendsInit();
			}
			catch( Exception e )
			{
				throw new NobsException( e );
			}
		}
	}

	protected void appOnceInitAdminUserCreate()
	{
		String encPasswd;
		NobsUser user;
		
		user = new NobsUser();
		user.setUserid( 
				JumbalakkaPropertyPlaceHolderConfigure
					.getPropertyValue( "onetime.admin.user" ) );
		encPasswd = 
				JumbalakkaSecurityUtils.
					encryptPassword( JumbalakkaPropertyPlaceHolderConfigure
							.getPropertyValue( "onetime.admin.password" ) );
		user.setPassword( encPasswd );
		user.setUserName( 
				JumbalakkaPropertyPlaceHolderConfigure
					.getPropertyValue( "onetime.admin.username" ) );
		
		user.setStatus( USERSTATUS.ACTIVE.toString() );
		getHibernateTemplate().save( user );
	}
	
	
	protected void appOnceInitUserCreate()
	{
		String encPasswd;
		NobsUser user;
		
		int maxCreate = NumberUtils.toInt(  
				JumbalakkaPropertyPlaceHolderConfigure.getPropertyValue( "onetime.default.count" ) );
		Map< String, String > authuser = new HashMap< String, String >();
		for( int i=0; i<maxCreate; i++ )
		{
			user = new NobsUser();
			user.setUserid( 
					JumbalakkaPropertyPlaceHolderConfigure
						.getPropertyValue( "onetime.default.user" + i ) );
			
			encPasswd = 
					JumbalakkaSecurityUtils.
						encryptPassword( JumbalakkaPropertyPlaceHolderConfigure
								.getPropertyValue( "onetime.default.password" + i ) );
			user.setPassword( encPasswd );
			user.setUserName( 
					JumbalakkaPropertyPlaceHolderConfigure
						.getPropertyValue( "onetime.default.username" + i ) );
			
			authuser.put( user.getUserid(), user.getUserName() );
			
			user.setStatus( USERSTATUS.ACTIVE.toString() );
			getHibernateTemplate().save( user );
		}
		
		/*
		String authType = "";
		
		for( String key: authuser.keySet() )
		{
			authType = "nobs.auth.friends." + key ;
			for( String keyInner: authuser.keySet() )
			{
				//user should also be in the list
				//if( StringUtils.equals( key, keyInner ) == false )	
				//{
					tupleDAO.addTuple( authType, keyInner, authuser.get( keyInner ) );
				//}
			}
		}*/
	}
	
	protected void oneTimeaddFriendsInit()
	{
		String encPasswd;
		NobsUser user;
		
		int maxCreate = NumberUtils.toInt(  
				JumbalakkaPropertyPlaceHolderConfigure.getPropertyValue( "onetime.default.count" ) );
		Map< String, String > authuser = new HashMap< String, String >();
		for( int i=0; i<maxCreate; i++ )
		{
			user = new NobsUser();
			user.setUserid( 
					JumbalakkaPropertyPlaceHolderConfigure
						.getPropertyValue( "onetime.default.user" + i ) );
			
			user.setUserName( 
					JumbalakkaPropertyPlaceHolderConfigure
						.getPropertyValue( "onetime.default.username" + i ) );
			
			authuser.put( user.getUserid(), user.getUserName() );
			
			user.setStatus( USERSTATUS.ACTIVE.toString() );
		}
		
		String authType = "";
		
		for( String key: authuser.keySet() )
		{
			authType = "nobs.auth.friends." + key ;
			for( String keyInner: authuser.keySet() )
			{
				//user should also be in the list
				//if( StringUtils.equals( key, keyInner ) == false )	
				//{
					tupleDAO.addTuple( authType, keyInner, authuser.get( keyInner ) );
				//}
			}
		}
	}
	
	public NobsUser getUser( String userId ) throws NobsException
	{
		DetachedCriteria criteria = DetachedCriteria.forClass( NobsUser.class );
		criteria.add( Restrictions.eq( "userid", userId ) );
		
		List<NobsUser> users = getHibernateTemplate().findByCriteria( criteria );
		if( users.isEmpty() == false )
		{
			if( users.size() > 1 )
			{
				throw new NobsException( "Data Inconsistent: Cannot have more than one user" );
			}
			else
			{
				NobsUser user =  users.get( 0 );
				user.setGroups( getUsersGroup( user ) );
				return user;
			}
		}
		throw new NobsException( "No such user found" );
	}
	
	public List<NobsUsersGroup> getUsersGroup( NobsUser user ) throws NobsException
	{
		DetachedCriteria criteria = DetachedCriteria.forClass( NobsUsersGroup.class );
		criteria.add( Restrictions.eq( "user", user ) );
		criteria.setFetchMode( "group", FetchMode.JOIN );
		return getHibernateTemplate().findByCriteria( criteria );
	}
	
	
	public String getValidPasswordPassword( String newPassword ) throws NobsException
	{
		if( StringUtils.isNotBlank( newPassword ) && newPassword.length() > 5 )
		{
			return JumbalakkaSecurityUtils.encryptPassword( newPassword );
		}
		throw new NobsException( "Password should be greater than five!" );
	}
	
	public void updateUser( NobsUser user )
	{
		getHibernateTemplate().update( user );
	}
	
	public void updateUserPassword( NobsUser user, String newPassword ) throws NobsException
	{
		user.setPassword( getValidPasswordPassword( newPassword ) );
		getHibernateTemplate().update( user );
	}
	
	/* (non-Javadoc)
	 * @see com.jumbalakka.nobs.dao.db.impl.NobsDAO#authUser(java.lang.String, java.lang.String)
	 */
	public void authUser( String uname, String password ) throws NobsException
	{
		DetachedCriteria criteria = DetachedCriteria.forClass( NobsUser.class );
		criteria.add( Restrictions.eq( "userid", uname ) );
		//criteria.setFetchMode( "password", FetchMode.JOIN );
		List< NobsUser > users = getHibernateTemplate().findByCriteria( criteria );
		if( users.isEmpty() == false )
		{
			if( users.size() > 1 )
			{
				throw new NobsException( "Data Inconsistent: Cannot have more than one user" );
			}
			else if( StringUtils.equals( users.get(0).getStatus(), USERSTATUS.ACTIVE.toString() ) == false )
			{
				throw new NobsException( "User not registered to use this system" );
			}
			else if( JumbalakkaSecurityUtils.validPassword( password, users.get( 0 ).getPassword() ) )
			{
				return;
			}
			
		}
		throw new NobsException( "Invalid user credentials" );
	}
	
	public void logInfo( NobsUser user, String ref, String app, String message )
	{
		NobsAuditLog log = createLog( user, ref, app, message, LOGTYPE.INFO.toString() );
		getHibernateTemplate().save( log );
	}

	public void logError( NobsUser user, String ref, String app, String message )
	{
		NobsAuditLog log = createLog( user, ref, app, message, LOGTYPE.ERROR.toString() );
		getHibernateTemplate().save( log );
	}
	
	private NobsAuditLog createLog( NobsUser user, String ref, String app,
			String message, String type )
	{
		NobsAuditLog log = new NobsAuditLog();
		log.setApp( app );
		log.setCreateDate( new Date() );
		log.setMessage( message );
		log.setReference( ref );
		log.setType( type );
		log.setUserId( user.getUserid() );
		log.setUserName( user.getUserName() );
		return log;
	}
	
	public List<Tuple> getAllAuthroizedFriend( NobsUser user )
	{
		String type = 
				JumbalakkaPropertyPlaceHolderConfigure.getPropertyValue( "nobs.auth.friends" ) + "." +
						user.getUserid(); 
		return tupleDAO.getByType( type ) ;
	}
	
	public void addNewBill( NobsUser reporter, String title, String desc, double cost, List< NobsUser > payee )
	{
		NobsBillHeader hdr = new NobsBillHeader();
		hdr.setDateCreated( new Date() );
		hdr.setDescription( desc );
		hdr.setTitle( title );
		getHibernateTemplate().save( hdr );
		NobsBillLine line = new NobsBillLine();
		line.setCost( cost );
		line.setDescription( desc );
		line.setHeader( hdr );
		line.setPayee( reporter );
		line.setDateCreated( new Date() );
		line.setTitle( title );
		getHibernateTemplate().save( line );
		NobsLinePayers payer = null;
		double dividedCost = 0d;
		if( cost> 0 && payee.size() > 0 )
		{
			dividedCost = cost/payee.size();
		}
		for( NobsUser payingUser: payee )
		{
			payer = new NobsLinePayers();
			payer.setBillLine( line );
			payer.setPayer( payingUser );
			payer.setPays( dividedCost );
			getHibernateTemplate().save( payer );
		}
	}
	
}
