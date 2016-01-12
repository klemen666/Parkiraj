package si.uni_lj.fe.seminarTK.dao;
import java.sql.*;

import org.codehaus.jettison.json.JSONArray;

import si.uni_lj.fe.seminarTK.util.ToJSON;

public class Schema_SQL extends SQL_data  {
	
	/**
	 * This method will insert a record into the PC_PARTS table. 
	 * 
	 * Note: there is no validation being done... if this was a real project you
	 * must do validation here!
	 * 
	 * @param PC_PARTS_TITLE
	 * @param PC_PARTS_CODE
	 * @param PC_PARTS_MAKER
	 * @param PC_PARTS_AVAIL - integer column
	 * @param PC_PARTS_DESC
	 * @return integer 200 for success, 500 for error
	 * @throws Exception
	 */
	public int insertLocation(String latitude, 
							String longitude, 
							String address, 
							long startTime)	
										throws Exception {

		PreparedStatement query = null;
		Connection conn = null;

		try {
			/*
			 * If this was a real application, you should do data validation here
			 * before starting to insert data into the database.
			 * 
			 * Important: The primary key on PC_PARTS table will auto increment.
			 * 		That means the PC_PARTS_PK column does not need to be apart of the 
			 * 		SQL insert query below.
			 */
			conn = sqlConnection();
			query = conn.prepareStatement("insert into parkinglocations " +
					"(latitude, longitude, address, startTime) " +
					"VALUES ( ?, ?, ?, ? ) ");

			double latitudeDouble = Double.parseDouble(latitude);
			query.setDouble(1, latitudeDouble);
			double longitudeDouble = Double.parseDouble(longitude);
			query.setDouble(2, longitudeDouble);
			
			
			query.setString(3, address);
			query.setLong(4, startTime);

			//PC_PARTS_AVAIL is a number column, so we need to convert the String into a integer
			//int avilInt = Integer.parseInt(PC_PARTS_AVAIL);
			//query.setInt(4, avilInt);

			//query.setString(5, PC_PARTS_DESC);
			query.executeUpdate(); //note the new command for insert statement

		} catch(Exception e) {
			e.printStackTrace();
			return 500; //if a error occurs, return a 500
		}
		finally {
			if (conn != null) conn.close();
		}

		return 200;
	}	
	
	
public JSONArray queryReturnLocations(int parkingID) throws Exception {
		
		PreparedStatement query = null;
		Connection conn = null;
		
		ToJSON converter = new ToJSON();
		JSONArray json = new JSONArray();
		
		try {
			conn = sqlConnection();
			query = conn.prepareStatement("select parkingID, latitude, longitude, address, startTime " +
											"from parkinglocations " +
											"where parkingID = ? ");
			
			query.setInt(1, parkingID); //protect against sql injection
			ResultSet rs = query.executeQuery();
			
			json = converter.toJSONArray(rs);
			query.close(); //close connection
		}
		catch(SQLException sqlError) {
			sqlError.printStackTrace();
			return json;
		}
		catch(Exception e) {
			e.printStackTrace();
			return json;
		}
		finally {
			if (conn != null) conn.close();
		}
		
		return json;
	}

public JSONArray queryReturnLocations2(int parkingID, String address) throws Exception {
	
	PreparedStatement query = null;
	Connection conn = null;
	
	ToJSON converter = new ToJSON();
	JSONArray json = new JSONArray();
	
	try {
		conn = sqlConnection();
		query = conn.prepareStatement("select parkingID, latitude, longitude, address, startTime " +
										"from parkinglocations " +
										"where parkingID = ? " +
										"and address = ?");
		
		/*
		 * protect against sql injection
		 * when you have more than one ?, it will go in chronological
		 * order.
		 */
		query.setInt(1, parkingID); //first ?
		query.setString(2, address); //second ?
		ResultSet rs = query.executeQuery();
		
		json = converter.toJSONArray(rs);
		query.close(); //close connection
	}
	catch(SQLException sqlError) {
		sqlError.printStackTrace();
		return json;
	}
	catch(Exception e) {
		e.printStackTrace();
		return json;
	}
	finally {
		if (conn != null) conn.close();
	}
	
	return json;
}

public int updateLocation(int parkingID, int avail) throws Exception {
	
	PreparedStatement query = null;
	Connection conn = null;
	
	try {
		/*
		 * If this was a real application, you should do data validation here
		 * before updating data.
		 */
		
		conn = sqlConnection();
		query = conn.prepareStatement("update parkinglocations " +
										"set endTime = ? " +
										"where parkingID = ? ");
		
		query.setInt(1, avail);
		query.setInt(2, parkingID);
		
		query.executeUpdate();
		
	} catch(Exception e) {
		e.printStackTrace();
		return 500;
	}
	finally {
		if (conn != null) conn.close();
	}
	
	return 200;
}
}
