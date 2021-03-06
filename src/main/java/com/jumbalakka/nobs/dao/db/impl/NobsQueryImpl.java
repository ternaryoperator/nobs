package com.jumbalakka.nobs.dao.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.jumbalakka.nobs.exception.NobsException;
import com.jumbalakka.nobs.type.NobsBillLine;
import com.jumbalakka.nobs.type.NobsLinePayers;
import com.jumbalakka.nobs.type.NobsResult;
import com.jumbalakka.nobs.type.NobsUser;

public class NobsQueryImpl extends JdbcTemplate
{
	public List<NobsResult> getFriendsPay( NobsUser user )
	{
		String sql = ""
				+ "SELECT Sum(p.user_pays_amt), "
				+ "       p.user_payer, "		//friend owing
				+ "       u.nobs_username "		//his usernmae	
				+ "FROM   nobs_bill_line_payers p "
				+ "       JOIN nobs_bill_line l "
				+ "         ON ( p.bill_line_ref = l.id ) "
				+ "       JOIN nobs_user u "
				+ "         ON ( p.user_payer = u.nobs_userid ) "
				+ "       JOIN nobs_user u1 "
				+ "         ON ( u1.nobs_userid = l.nobs_line_payee ) "
				+ "WHERE  u1.nobs_userid = ? "
				+ "       AND u1.nobs_userid != u.nobs_userid "
				+ "GROUP  BY p.user_payer, "
				+ "          u.nobs_username ";
		return query( sql, new Object[]{ user.getUserid() } ,new NobsSummatorMapper() );
	}
	
	public List<NobsResult> getYouOwe( NobsUser user )
	{
		String sql = ""
				+ "SELECT Sum(p.user_pays_amt), "
				+ "       p.user_payer, "
				+ "       u.nobs_username "
				+ "FROM   nobs_bill_line_payers p "
				+ "       JOIN nobs_bill_line l "
				+ "         ON ( p.bill_line_ref = l.id ) "
				+ "       JOIN nobs_user u "
				+ "         ON ( l.nobs_line_payee = u.nobs_userid ) "
				+ "       JOIN nobs_user u1 "
				+ "         ON ( u1.nobs_userid = p.user_payer ) "
				+ "WHERE  u1.nobs_userid = ? "
				+ "       AND u1.nobs_userid != u.nobs_userid "
				+ "GROUP  BY p.user_payer, "
				+ "          u.nobs_username ";
		return query( sql, new Object[]{ user.getUserid() }, new NobsSummatorMapper() );
	}
	
	public List<NobsResult> getAllResult( NobsUser user, int firstResult, int maxResult )
	{
		int lastResult = firstResult + maxResult;
		String sql = ""			//FRIENDS PAY
				+ "(SELECT Count(*), "
				+ "       l.nobs_line_title, "
				+ "       l.nobs_line_desc, "
				+ "       l.bill_line_date_created, "
				+ "       p.user_pays_amt, "
				+ "       l.nobs_line_amt, "
				+ "       u1.nobs_username, "				//payee
				+ "       u1.nobs_userid, "
				+ "       u.nobs_userid, "					//you
				+ "       l.id, "							//you
				+ "       '1' "
				+ "FROM   nobs_bill_line_payers p "
				+ "       JOIN nobs_bill_line l "
				+ "         ON ( p.bill_line_ref = l.id ) "
				+ "       JOIN nobs_user u "
				+ "         ON ( u.nobs_userid = l.nobs_line_payee ) "
				+ "       JOIN nobs_user u1 "
				+ "         ON ( u1.nobs_userid = p.user_payer ) "
				+ "WHERE  u.nobs_userid = ? and u.nobs_userid != u1.nobs_userid "
				+ "GROUP  BY l.nobs_line_title, "
				+ "          l.nobs_line_desc, "
				+ "          l.bill_line_date_created, "
				+ "          p.user_pays_amt, "
				+ "          l.nobs_line_amt, "
				+ "          u1.nobs_username, "
				+ "          u1.nobs_userid, "
				+ "       	 u.nobs_userid, "
				+ "       	 l.id, "
				+ "          '1' "
				+ "ORDER BY l.bill_line_date_created DESC LIMIT " + firstResult + "," + lastResult + ") "
				+ "UNION "			//OWE
				+ "(SELECT Count(*), "					//1
				+ "       l.nobs_line_title, "			//2
				+ "       l.nobs_line_desc, "			//3
				+ "       l.bill_line_date_created, "	//4
				+ "       p.user_pays_amt, "			//5
				+ "       l.nobs_line_amt, "			//6
				+ "       u1.nobs_username, "			//7			//payto
				+ "       u1.nobs_userid, "				//8
				+ "       u.nobs_userid, "				//9			//you
				+ "       l.id, "						//10
				+ "       '2' "							//11
				+ "FROM   nobs_bill_line_payers p "
				+ "       JOIN nobs_bill_line l "
				+ "         ON ( p.bill_line_ref = l.id ) "
				+ "       JOIN nobs_user u "
				+ "         ON ( u.nobs_userid = p.user_payer ) "
				+ "       JOIN nobs_user u1 "
				+ "         ON ( u1.nobs_userid = l.nobs_line_payee ) "
				+ "WHERE  u.nobs_userid = ? and u.nobs_userid != u1.nobs_userid "
				+ "GROUP  BY l.nobs_line_title, "
				+ "          l.nobs_line_desc, "
				+ "          l.bill_line_date_created, "
				+ "          p.user_pays_amt, "
				+ "          l.nobs_line_amt, "
				+ "          u1.nobs_username, "
				+ "          u1.nobs_userid, "
				+ "       	 u.nobs_userid, "
				+ "       	 l.id, "
				+ "          '2' "
				+ "ORDER BY l.bill_line_date_created DESC LIMIT " + firstResult + "," + lastResult + ") ";
		return query( sql, new Object[]{ user.getUserid(), user.getUserid() }, new NobsResultMapper() );
	}
	
	public void deleteBillLine( NobsUser currentUser, int lineId ) throws NobsException
	{
		String sql;
		sql = "select count(*) from NOBS_BILL_LINE where ID = ? AND NOBS_LINE_PAYEE= ?";
		List<Integer> result = query( sql, new Object[]{ lineId, currentUser.getUserid() }, new NobsCount1() );
		if( result.isEmpty() == false )
		{
			sql = "delete from NOBS_BILL_LINE_PAYERS where BILL_LINE_REF = ?";
			update( sql, new Object[]{ lineId } );
			sql = "select NOBS_HDR_REF  from NOBS_BILL_LINE where ID = ?";
			result = query( sql, new Object[]{ lineId }, new NobsCount1() );
			sql = "delete from NOBS_BILL_LINE where ID = ?";
			update( sql, new Object[]{ lineId } );
			for( Integer idToDel: result )
			{
				sql = "delete from NOBS_BILL_HDR where ID =?";
				update( sql, new Object[]{ idToDel } );
			}
			
		}
		else
		{
			throw new NobsException( "Only owner can delete" );
		}
	}
	
	public class NobsCount1 implements RowMapper
	{
		public Object mapRow( ResultSet rs, int rowNum ) throws SQLException
		{
			return rs.getInt( 1 );
		}
	}
	public class NobsSummatorMapper implements RowMapper
	{
		public Object mapRow( ResultSet rs, int rowNum ) throws SQLException
		{
			NobsResult n = new NobsResult();
			n.setCost( rs.getDouble( 1 ) );
			n.setPayer( rs.getString( 3 ) );
			return n;
		}
	}
	
	public class NobsResultMapper implements RowMapper
	{
		public Object mapRow( ResultSet rs, int rowNum ) throws SQLException
		{
			NobsResult n = new NobsResult();
			int index = 1;
			int count = rs.getInt( 1 );
			n.setTitle( rs.getString( 2 ) );
			n.setDescription( rs.getString( 3 ) );
			
			if( rs.getDate( 4 ) != null  )
			{
				n.setDateCreated( new Date( rs.getDate( 4 ).getTime() ) );
			}
			n.setCost( (Double) rs.getObject( 5 ) );
			n.setPayer( rs.getString( 7 ) ); 
			n.setId( rs.getInt( 10 ) );
			if ( StringUtils.equals( rs.getString( 11 ), "2" ) )
			{
				n.setType( NobsResult.PAYEE_TYPE.OWE.toString() );
			}
			else
			{
				n.setType( NobsResult.PAYEE_TYPE.GETPAID.toString() );
			}
			return n;
		}
		
	}
}


