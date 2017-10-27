The project OpenRobertaServer contains the server logic, that accesses
- a database with Hibernate-based DAO objects
- plugins for various robots which are supported in OpenRoberta
- services for browser-based clients
- services for robots connected to the lab either by Wifi or USB

The server is made of
- an embedded jetty server exposing REST services
- the services are based on jersey
- JSON (sometimes XML or plain text) is sent by a browser client and JSON is returned by the server
- JSON (sometimes XML or plain text) is sent by a robot and JSON or binary data is returned by the server

Furthermore, this project contains in directory staticResources for the browser client
- HTML and CSS
- Javascript libraries based on jquery and bootstrap for
  - assertions (DBC)
  - ajax-based server calls (COMM)
  - logging (LOG) and
  - javascript resources for blockly (see: http://code.google.com/p/blockly/)
