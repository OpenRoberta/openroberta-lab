define([ 'exports', 'util', 'log', 'message', 'jquery', 'guiState.controller', 'socket.io' ], function(exports, UTIL, LOG, MSG, $, GUISTATE_C, IO) {
	
	var socket = IO('ws://localhost:8991/');
	var portList = [];
	var vendorList = []; 
	var productList = [];
	var system;
	var cmd;
	
	function init() {

		socket.on('connect', function(){
		    console.log('connect');
			  socket.emit('command', 'log on');
			  socket.emit('command', 'list');
			  console.log('listed');
		});
		socket.on('message', function(data){
		   if(data.includes('"Network": false')) {
		        jsonObject = JSON.parse(data);
		        var i = 0;
		  	    while (jsonObject['Ports'][i] != null){ 
			        portList.push(jsonObject['Ports'][i]['Name'])
			        vendorList.push(jsonObject['Ports'][i]['VendorID'])
			        productList.push(jsonObject['Ports'][i]['ProductID'])
			        i++;
		  	    }
		  	    if (i === 1) {
		  	    	GUISTATE_C.setConnected(true);
		  	    }
		  	    else{
		  	    	GUISTATE_C.setConnected(false);
		  	    }
		  	    console.log(portList);
		  	    console.log(vendorList);
		  	    console.log(productList);
		    }
		   else if (data.includes('OS')) {
			   jsonObject = JSON.parse(data);
			   system = jsonObject['OS'];
			   console.log(system);
		   }
		});

		socket.on('disconnect', function(){});
		
	}
	
	exports.init = init;
	
	function getPortList(){
		return portList;
	}
	exports.getPortList = getPortList;
	
});


