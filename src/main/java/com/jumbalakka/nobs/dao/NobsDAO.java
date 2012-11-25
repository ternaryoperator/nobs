package com.jumbalakka.nobs.dao;

import java.util.List;
import com.jumbalakka.commons.config.type.Tuple;
import com.jumbalakka.nobs.exception.NobsException;
import com.jumbalakka.nobs.type.NobsResult;
import com.jumbalakka.nobs.type.NobsUser;

public interface NobsDAO
{
	/*
	public List<NobsResult> getAllResult( NobsUser user, int firstResult, int maxResult );
	
	public List< NobsResult > getFriendsPay( NobsUser user, int firstResult,
			int maxResult );
	
	public List< NobsResult > getYouOwe( NobsUser user, int firstResult,
			int maxResult );
	*/
	public void addNewBill( NobsUser reporter, String title, String desc, double cost, List< NobsUser > payee );
	
	public String getValidPasswordPassword( String newPassword ) throws NobsException;
	
	public void updateUserPassword( NobsUser user, String newPassword ) throws NobsException;
	
	public void updateUser( NobsUser user );
	
	public NobsUser getUser( String userId ) throws NobsException;
	
	public void logInfo( NobsUser user, String ref, String app, String message );
	
	public void logError( NobsUser user, String ref, String app, String message );
	
	/**
	 * should be called for one time init of app
	 * @throws NobsException
	 */
	public void appOnceInit() throws NobsException;

	/**
	 * should be called to auth user
	 * on no error user auth passed
	 * 
	 * @param uname
	 * @param password
	 * @throws NobsException
	 */
	public void authUser( String uname, String password ) throws NobsException;
	
	public List<Tuple> getAllAuthroizedFriend( NobsUser user );
}