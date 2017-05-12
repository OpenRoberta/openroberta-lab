define([ 'exports', 'util', 'log', 'message', 'jquery', 'guiState.controller', 'socket.io' ], function(exports, UTIL, LOG, MSG, $, GUISTATE_C, IO) {
	
	function init() {
		var socket = IO('ws://localhost:8991/');
		var portList = [];
		var vendorList = []; //ProductID ?
		var system;
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
			        i++;
		  	    }
		  	    console.log(portList);
		  	    console.log(vendorList);
		    }
		   else if (data.includes('OS')) {
			   jsonObject = JSON.parse(data);
			   console.log(jsonObject['OS']);
		   }
		});

		socket.on('disconnect', function(){});
	}
	
	exports.init = init;
});
