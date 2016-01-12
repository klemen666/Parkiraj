package si.uni_lj.fe.seminarTK.dao;

import javax.naming.*;
import java.sql.Connection;
import javax.sql.*;

public class SQL_data {

	private static DataSource mySql = null;
	private static Context context = null;
	
	public static DataSource mySqlConn() throws Exception{
		if(mySql != null){
			
			return mySql;
		}
		
		try{
			if(context == null){
				context = new InitialContext();
			}
			//Class.forName("com.mysql.jdbc.Driver");
			mySql = (DataSource) context.lookup("java:comp/env/jdbc/MySQLDB");
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		return mySql;

	}
	
	protected static Connection sqlConnection(){
		Connection conn = null;
		try{
			conn = mySqlConn().getConnection();
			return conn;
		}
		catch (Exception e){
			e.printStackTrace();
			
		}
	return conn;	
	}
	
}
