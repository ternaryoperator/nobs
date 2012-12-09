package com.jumbalakka.nobs.web.tag;

import java.util.List;
import javax.servlet.jsp.JspException;
import com.jumbalakka.commons.config.JumbalakkaPropertyPlaceHolderConfigure;
import com.jumbalakka.commons.config.dao.TupleDAO;
import com.jumbalakka.commons.config.type.Tuple;
import com.jumbalakka.commons.spring.SpringContextUtils;
import com.jumbalakka.commons.web.JumbalakkaAbstractTag;
import com.jumbalakka.nobs.dao.NobsDAO;
import com.jumbalakka.nobs.type.NobsLinePayers;
import com.jumbalakka.nobs.type.NobsUser;

public class FriendsMultiSelectTag extends JumbalakkaAbstractTag
{
	String compName;
	String compId;
	String styleClass;
	String compField;
	List< NobsLinePayers > payers;
	boolean multiSelect = true;
	
	public boolean isMultiSelect()
	{
		return multiSelect;
	}

	public void setMultiSelect( boolean multiSelect )
	{
		this.multiSelect = multiSelect;
	}

	public String getCompField()
	{
		return compField;
	}

	public void setCompField( String compField )
	{
		this.compField = compField;
	}

	public String getCompName()
	{
		return compName;
	}

	public void setCompName( String compName )
	{
		this.compName = compName;
	}

	public String getCompId()
	{
		return compId;
	}

	public void setCompId( String compId )
	{
		this.compId = compId;
	}

	public String getStyleClass()
	{
		return styleClass;
	}

	public void setStyleClass( String styleClass )
	{
		this.styleClass = styleClass;
	}

	public List< NobsLinePayers > getPayers()
	{
		return payers;
	}

	public void setPayers( List< NobsLinePayers > payers )
	{
		this.payers = payers;
	}

	@Override
	protected int doJumbEndTag() throws JspException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int doJumbStartTag() throws JspException
	{
		NobsUser user = getSessionAttribute( USER_SESSION, NobsUser.class );
		NobsDAO dao = SpringContextUtils.getBean( "nobsDAO", NobsDAO.class );
		List<Tuple> tuples = dao.getAllAuthroizedFriend( user ) ;
				
		StringBuffer buffer = new StringBuffer();		
		buffer.append( "<select name='").append( compName ).append( "' " )
			.append( "id='" ).append( compId ).append( "' " )
			.append( "class='" ).append( styleClass ).append( "' " )
			.append( "data-field-name='" ).append( compField ).append( "' " );
		if( multiSelect )
		{
			buffer.append( "multiple>" );
		}
		else
		{
			buffer.append( ">" );
		}
		for( Tuple t: tuples )
		{
			//<option value='adsd'>value</option>
			buffer.append( "<option value='" ).append( t.getKey() ).append( "' " );
			buffer.append( ">" ).append( t.getValue() ).append( "</option>" );
			
		}
		buffer.append( "</select>" );
		if( tuples.isEmpty() == false )
		{
			write( buffer.toString() );
		}
		return 0;
	}

	@Override
	protected void jumbRelease()
	{
		// TODO Auto-generated method stub
		
	}
	
}
