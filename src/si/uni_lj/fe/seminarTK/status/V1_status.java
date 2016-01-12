package si.uni_lj.fe.seminarTK.status;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;

import si.uni_lj.fe.seminarTK.dao.*;

@Path("v1/status/")
public class V1_status {
	private static final String api_version = "1.00";
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle(){
		
	return "<h1>Java web service</h1>";
		
	}
	@Path("version")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnVersion(){
		
	return "<p>Version: " + api_version +"</p>";
		
	}
	
	@Path("/database")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnDatabaseStatus() throws Exception {
		
		PreparedStatement query = null;
		String myString = null;
		String returnString = null;
		Connection conn = null;
		
		try {
			
			conn = SQL_data.mySqlConn().getConnection(); //calls the method defined in the Oracle308tube package
			
			//simple sql query to return the date/time
			query = conn.prepareStatement("select address from parkinglocations");
			ResultSet rs = query.executeQuery();
			
			//loops through the results and save it into myString
			while (rs.next()) {
				// /*Debug*/ System.out.println(rs.getString("DATETIME"));
				myString = rs.getString("address");
			}
			
			query.close(); //close connection
			
			returnString = "<p>Database Status</p> " +
				"<p>Database Date/Time return: " + myString + "</p>";
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * The finally cause will always run. Even if the the method get a error.
		 * You want to make sure the connection to the database is closed.
		 */
		finally {
			if (conn != null) conn.close();
		}
		
		return returnString; 
	}
	
	
		
	
	
}
