	var scene, camera, renderer;
	var geometry, material, roberta;
	var obstical;
		
	var MOVEMENT_STEP = 10; 
	var ROBERTA_X = 200;
	var ROBERTA_Y = 200;
	var robertaMaterials;
	
	var meshArray = [];
	var collusionCount = 0;
	var collisionDirection;	    //North   East	 South	West
	var collisionDirectionArray = [false, false, false, false];
	var collisionDirectionArrayIndex;

	init();
	animate();

	function init() {
		
		scene = new THREE.Scene();
		
		camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 1, 10000 );
		camera.position.z = 1000;
		
		createRoberta();
		createMeshsForScene();
		
		renderer = new THREE.CanvasRenderer();
		renderer.setSize( window.innerWidth, window.innerHeight );
		renderer.setClearColor( 0xffffff, 1);
		
		$( "#simulatorRender" ).append( renderer.domElement );
	}
	
function clear(){
	renderer.clear();
}

function animate() {

	requestAnimationFrame( animate );

	renderer.render( scene, camera );
}

function createRoberta(){
	geometry = new THREE.BoxGeometry( ROBERTA_X, ROBERTA_Y, 0 );
	
	var material1 = new THREE.MeshLambertMaterial( { map: THREE.ImageUtils.loadTexture('./css/img/simulator/dummy_up.png') } );
    var material2 = new THREE.MeshLambertMaterial( { map: THREE.ImageUtils.loadTexture('./css/img/simulator/dummy_down.png') } );
    var material3 = new THREE.MeshLambertMaterial( { map: THREE.ImageUtils.loadTexture('./css/img/simulator/dummy_left.png') } );
    var material4 = new THREE.MeshLambertMaterial( { map: THREE.ImageUtils.loadTexture('./css/img/simulator/dummy_right.png') } );

    var robertaMaterials = [material1, material2, material3, material4];
 
    var meshFaceMaterial = new THREE.MeshFaceMaterial( robertaMaterials );

	//material = new THREE.MeshBasicMaterial({color: 0x00ff00});
	
	roberta = new THREE.Mesh( geometry, robertaMaterials[0] );
	roberta.overdraw = true;
	scene.add( roberta );
}

function createMeshsForScene(){
	
	var obgeo = new THREE.BoxGeometry( 200, 200, 200);
	var obmat =  new THREE.MeshLambertMaterial({
									map: THREE.ImageUtils.loadTexture('http://www.html5canvastutorials.com/demos/assets/crate.jpg')
				});

	var step = 0;
	
	for(var i = 0; i < 8; i++){
		
		obstical = new THREE.Mesh( obgeo, obmat );
		obstical.overdraw = true;
		
		obstical.position.x = -400;
		obstical.position.y = 230 + step;
		step += 200;
		
		scene.add( obstical );
		meshArray.push( obstical );
	}
	
	step = 0;
	
	for(var i = 0; i < 5; i++){
		
		obstical = new THREE.Mesh( obgeo, obmat );
		obstical.overdraw = true;
		
		obstical.position.x = 400;
		obstical.position.y = 230 + step;
		step += 200;
		
		scene.add( obstical );
		meshArray.push( obstical );
		
	}
	
	step = 0;
	
	for(var i = 0; i < 9; i++){
		
		obstical = new THREE.Mesh( obgeo, obmat );
		obstical.overdraw = true;
		
		obstical.position.x = -400 + step;
		obstical.position.y = 1830;
		step += 200;
		
		scene.add( obstical );
		meshArray.push( obstical );
	}
	
	step = 0;
	
	for(var i = 0; i < 8; i++){
		
		obstical = new THREE.Mesh( obgeo, obmat );
		obstical.overdraw = true;
		
		obstical.position.x = 1200;
		obstical.position.y = 230 + step;
		step += 200;
		
		scene.add( obstical );
		meshArray.push( obstical );
	}
	
}

// Change Roberta mesh material

function changeRobertaMaterial(index){
	roberta.materials = robertaMaterials[index];
}

// Move meshes in x or y direction.
	
function moveRoberta(cmd){
		
	if(cmd != collisionDirection){
		
		// Clear the variable.
		
		collisionDirection = "";
		
		for(var i = 0; i <  meshArray.length; i++){
			
			if(cmd == "left" && collisionDirectionArray[3] == false){
				//changeRobertaMaterial(2);
				meshArray[i].position.x += MOVEMENT_STEP;
				clearCollisionDirection();
			}
			
			else if(cmd == "right" && collisionDirectionArray[1] == false){
				//changeRobertaMaterial(3);
				meshArray[i].position.x -= MOVEMENT_STEP;
				clearCollisionDirection();

			}
			
			else if(cmd == "up" && collisionDirectionArray[0] == false){
				//changeRobertaMaterial(0);
				meshArray[i].position.y -= MOVEMENT_STEP;
				clearCollisionDirection();

			}
			
			else if(cmd == "down" && collisionDirectionArray[2] == false){
				//changeRobertaMaterial(1);
				meshArray[i].position.y += MOVEMENT_STEP;	
				clearCollisionDirection();
			}
		}
	}
	
	// Check for possible collision.
		
	detectCollision(cmd);
}

// Detection of the collision with objects.

function detectCollision(cmd) {
	
	// Current position of the Roberta. 
	
	var robertaPosistionFrameX = roberta.position.x + ROBERTA_X;
	var robertaPosistionFrameY = roberta.position.y + ROBERTA_Y;
	
	//console.log("Roberta: x=" + robertaPosistionFrameX + " y=" + robertaPosistionFrameY);
	
	var currentMeshFrameX;
	var currentMeshFrameY;
	
	// Check possible collision for each mesh.
		
	for(var meshIndex = 0; meshIndex < meshArray.length; meshIndex++){
		
		currentMeshFrameX = meshArray[meshIndex].position.x;
		currentMeshFrameY = meshArray[meshIndex].position.y;
		
		//console.log("Checking " + checkCount + " current obstical: x=" + currentMeshFrameX + " y=" + currentMeshFrameY);
		
		// Condition check for collision.
		
		if(currentMeshFrameX <= robertaPosistionFrameX && currentMeshFrameX >= (robertaPosistionFrameX * (-1)) && currentMeshFrameY <= robertaPosistionFrameY && currentMeshFrameY >= (robertaPosistionFrameY * (-1))){
										
			console.log("Captain, a collusion (Nr." + collusionCount + ") has been detected at x=" + currentMeshFrameX + " y=" + currentMeshFrameY + " for " + cmd + " command.");
				
			// Set the direction where the collision occurred. 
			
			if(collisionDirection != cmd){
				setCollisionDirection(cmd);
				collusionCount += 1;
				roberta.material.uniforms.texture.value = THREE.ImageUtils.loadTexture("./css/img/simulator/dummy_down.png");
				roberta.material.uniforms.texture.needsUpdate = true;
			}
		}
	}
}

function setCollisionDirection(direction){
	
	collisionDirection = direction;
	
	switch(collisionDirection){
	case "left":
		collisionDirectionArrayIndex = 3;
		break;
	case "right":
		collisionDirectionArrayIndex = 1;
		break;
	case "up":
		collisionDirectionArrayIndex = 0;
		break;
	case "down":
		collisionDirectionArrayIndex = 2;
		break;
	}
	
	collisionDirectionArray[collisionDirectionArrayIndex] = true;
	
	for(var i = 0; i < 4; i++){
		if(i != collisionDirectionArrayIndex){
			collisionDirectionArray[i] = false;
		}
	}
}

function clearCollisionDirection() {
	for(var i = 0; i < 4; i++){
		collisionDirectionArray[i] = false;
	}
}




