Licensed under the Apache License, Version 2.0 (the "License"); you may not use this repository/its files except in compliance with the License.                   
You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0                                 
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and        
limitations under the License.

This repository contains on the server side
- an embedded jetty server exposing REST services
- the services are based on jersey

- it is expected that the POST method is used, JSON/XML/Plain text is sent by a client and JSON is returned by the server

This repository contains on the client side (in directory staticResources, usable by almost all browsers)
- HTML and CSS for a simple application
- a small Javascript library based on jquery 2.0.3 and jquery-ui v1.10.3 with modules (as self-executing functions) for
  - assertions (DBC)
  - ajax-based server calls (COMM)
  - logging (LOG), see below
  - the javascript resources for blockly (see: http://code.google.com/p/blockly/)

This repository contains
- a Junit test case based on Selenium to test the Javascript library
- corresponding HTML, CSS and Javascript to support the test case

When the client side running in a browser accesses the server using JSON, logging data assembled in the browser since the last server contact is sent to the server
automatically.