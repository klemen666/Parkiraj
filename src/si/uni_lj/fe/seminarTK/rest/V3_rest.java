package si.uni_lj.fe.seminarTK.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import si.uni_lj.fe.seminarTK.dao.Schema_SQL;

@Path("/v3/inventory")
public class V3_rest {
	
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response addLocation2(String incomingData) throws Exception {
		
		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Schema_SQL dao = new Schema_SQL();
		
		try {
			
			/*
			 * We can create a new instance and it will accept a JSON string
			 * By doing this, we can now access the data.
			 */
			JSONObject locationData = new JSONObject(incomingData);
			System.out.println( "jsonData: " + locationData.toString() );
			
			/*
			 * In order to access the data, you will need to use one of the method in JSONArray
			 * or JSONObject.  I recommend using the optXXXX methods instead of the get method.
			 * 
			 * Example:
			 * partsData.get("PC_PARTS_TITLE");
			 * The example above will get you the data, the problem is, if PC_PARTS_TITLE does
			 * not exist, it will generate a java error.  If you are using get, you need to use
			 * the has method first partsData.has("PC_PARTS_TITLE");. 
			 * 
			 * Example:
			 * partsData.optString("PC_PARTS_TITLE");
			 * The optString example above will also return data but if PC_PARTS_TITLE does not
			 * exist, it will return a BLANK string.
			 * 
			 * partsData.optString("PC_PARTS_TITLE", "NULL");
			 * You can add a second parameter, it will return NULL if PC_PARTS_TITLE does not
			 * exist.
			 */
			int http_code = dao.insertLocation(locationData.optString("latitude"), 
					locationData.optString("longitude"), 
					locationData.optString("address"), 
					locationData.optLong("startTime"));
			
			if( http_code == 200 ) {
				/*
				 * The put method allows you to add data to a JSONObject.
				 * The first parameter is the KEY (no spaces)
				 * The second parameter is the Value
				 */
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", "Item has been entered successfully, Version 3");
				/*
				 * When you are dealing with JSONArrays, the put method is used to add
				 * JSONObjects into JSONArray.
				 */
				returnString = jsonArray.put(jsonObject).toString();
			} else {
				return Response.status(500).entity("Unable to enter Item").build();
			}
			
			System.out.println( "returnString: " + returnString );
			
		} catch(Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request").build();
		}
		
		return Response.ok(returnString).build();
	}
	
	@Path("/{parkingID}/{endTime}")
	@PUT
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateItem(@PathParam("parkingID") int parkingID,
									String incomingData) 
								throws Exception {
		
		System.out.println("incomingData: " + incomingData);
		System.out.println("brand: " + parkingID);
		//System.out.println("item_number: " + endTime);
		
		int pk;
		int avail;
		int http_code;
		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Schema_SQL dao = new Schema_SQL();
		
		try {
			
			JSONObject locationData = new JSONObject(incomingData); //we are using json objects to parse data
			//pk = locationData.optInt("locationHidden", 0);
			avail = locationData.optInt("endTime", 0);
			
			//call the correct sql method
			http_code = dao.updateLocation(parkingID, avail);
			
			if(http_code == 200) {
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", "Item has been updated successfully");
			} else {
				return Response.status(500).entity("Server was not able to process your request").build();
			}
			
			returnString = jsonArray.put(jsonObject).toString();
			
		} catch(Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request").build();
		}
		
		return Response.ok(returnString).build();
	}

}
