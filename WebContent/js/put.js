/**
 * js file for post.html
 * Please use modern web browser as this file will not attempt to be
 * compatible with older browsers. Use Chrome and open javascript console
 * or Firefox with developer console.
 * 
 * jquery is required
 */
$(document).ready(function() {
	
	var $put_example = $('#put_example')
		, $set_parkingID = $('#set_parkingID')
		, $set_endTime = $('#set_endTime');
	
	getInventory();
	
	$(document.body).on('click', ':button, .UPDATE_BTN', function(e) {
		//console.log(this);
		var $this = $(this)
			, locationHidden = $this.val()
			, $tr = $this.closest('tr')
			, parkingID = $tr.find('.parkingID').text()
			, latitude = $tr.find('.latitude').text()
			, longitude = $tr.find('.longitude').text()
			, address = $tr.find('.address').text()
			, startTime = $tr.find('.startTime').text()
			, endTime = $tr.find('.endTime').text();
		
		$('#set_locationHidden').val(locationHidden);
		$("#set_parkingID").text(parkingID);
		$("#set_latitude").text(latitude);
		$("#set_longitude").text(longitude);
		$("#set_address").text(address);
		$('#set_startTime').text(startTime);
		$('#set_endTime').val(endTime);
		
		
		$('#update_response').text("");
		
		//console.log(parkingID);
	});
	
	$put_example.submit(function(e) {
		e.preventDefault(); //cancel form submit
		
		var obj = $put_example.serializeObject()
			, parkingID = $set_parkingID.text()
			, endTime = $set_endTime.text();
		
		updateLocation(obj, parkingID);
	});
});

function updateLocation(obj, parkingID) {
	
	ajaxObj = {  
			type: "PUT",
			url: "http://localhost:8080/com.youtube.rest/api/v3/inventory/" + parkingID,
			data: JSON.stringify(obj), 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) {
				console.log(data);
				$('#update_response').text( data[0].MSG );
			},
			complete: function(XMLHttpRequest) {
				console.log( XMLHttpRequest.getAllResponseHeaders() );
				getInventory();
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

function getInventory() {
	
	var d = new Date()
		, n = d.getTime();
	
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/Parkiraj/api/v1/inventory", 
			data: "ts="+n, 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) { 
				//console.log(data);
				var html_string = "";
				
				$.each(data, function(index1, val1) {
					//console.log(val1);
					html_string = html_string + templateGetInventory(val1);
				});
				
				$('#get_inventory').html("<table border='1'>" + html_string + "</table>");
			},
			complete: function(XMLHttpRequest) {
				//console.log( XMLHttpRequest.getAllResponseHeaders() );
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

function templateGetInventory(param) {
	return '<tr>' +
				'<td class="parkingID">' + param.parkingID + '</td>' +
				'<td class="latitude">' + param.latitude + '</td>' +
				'<td class="longitude">' + param.longitude + '</td>' +
				'<td class="address">' + param.address + '</td>' +
				'<td class="startTime">' + param.startTime + '</td>' +
				'<td class="endTime">' + param.endTime + '</td>' +
				'<td class="CL_PC_PARTS_BTN"> <button class="UPDATE_BTN" value="' + param.locationHidden + '" type="button">Update</button> </td>' +
			'</tr>';
}