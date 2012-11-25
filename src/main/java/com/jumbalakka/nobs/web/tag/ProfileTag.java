package com.jumbalakka.nobs.web.tag;

import javax.servlet.jsp.JspException;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.lang.StringUtils;
import com.jumbalakka.commons.spring.SpringContextUtils;
import com.jumbalakka.commons.web.JumbalakkaAbstractTag;
import com.jumbalakka.nobs.dao.NobsDAO;
import com.jumbalakka.nobs.exception.NobsException;
import com.jumbalakka.nobs.type.NobsUser;

public class ProfileTag extends JumbalakkaAbstractTag
{
	String mode = MODE_VIEW;
	public static final String MODE_VIEW="VIEW";
	public static final String MODE_UPDATE="UPDATE";
	
	public void setMode( String mode )
	{
		this.mode = mode;
	}

	@Override
	protected int doJumbEndTag() throws JspException
	{
		if( StringUtils.equals( mode, MODE_UPDATE ) )
		{
			NobsUser user = getSessionAttribute( USER_SESSION, NobsUser.class );
			String password = getRequest().getParameter( "passwd" );
			String password1 = getRequest().getParameter( "passwd1" );
			String password2 = getRequest().getParameter( "passwd2" );
			String userId =  getRequest().getParameter( "userid" );
			String userName = getRequest().getParameter( "userName" );
			NobsDAO dao = SpringContextUtils.getBean( "nobsDAO", NobsDAO.class );
			
			if( StringUtils.equals( userId, user.getUserid() ) )
			{
				user.setUserName( userName );
				dao.updateUser( user );
				setInfoMessage( "Profile Updated!" );
				
				if( StringUtils.isNotBlank( password ) && StringUtils.isNotBlank( password1 ) && 
					StringUtils.equals( password, password1 ) == false && 
						StringUtils.equals( password1, password2 ) == true )
				{
					user.setPassword( password1 );
					try
					{
						dao.updateUserPassword( user, password1 );
						appendInfoMessage( "Password Updated!" );
					}
					catch ( NobsException e )
					{
						setErrorMessage( e.getMessage() );
						return EVAL_PAGE;
					}
				}
				else if( StringUtils.isNotBlank( password ) && StringUtils.isNotBlank( password1 ) &&  
						StringUtils.equals( password, password1 ) )
				{
					setErrorMessage( "New password cannot be same as old password" );
					return EVAL_PAGE;
				}
				else if( StringUtils.isNotBlank( password1 ) && StringUtils.isNotBlank( password2 ) &&  
						StringUtils.equals( password1, password2 ) == false )
				{
					setErrorMessage( "New password do not match" );
					return EVAL_PAGE;
				}
			}
			else
			{
				setErrorMessage( "You are not authorized to do that" );
			}
			//BeanUtilsBean2 beanUtils = new BeanUtilsBean2();
		}
		return 0;
	}

	@Override
	protected int doJumbStartTag() throws JspException
	{
		
		return 0;
	}

	@Override
	protected void jumbRelease()
	{
		// TODO Auto-generated method stub
		
	}

}
