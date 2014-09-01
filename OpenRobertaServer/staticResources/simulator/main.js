	var scene, camera, renderer;
	var geometry, material, roberta;
	var obstical;
		
	var MOVEMENT_STEP = 10;
	var ROBERTA_X = 200;
	var ROBERTA_Y = 200;
	
	var meshArray = [];
	var collusionCount = 0;
	var collisionStatus = false;

	init();
	animate();

	function init() {
		
		scene = new THREE.Scene();
		
		camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 1, 10000 );
		camera.position.z = 1000;
		
		createMeshsForScene();
		
		renderer = new THREE.CanvasRenderer();
		renderer.setSize( window.innerWidth, window.innerHeight );
		renderer.setClearColor( 0xffffff, 1);
		
		$( "#simulatorDiv" ).append( renderer.domElement );

	}

function animate() {

	requestAnimationFrame( animate );

	renderer.render( scene, camera );

}

function createMeshsForScene(){
		
	// ROBERTA
	
	geometry = new THREE.BoxGeometry( ROBERTA_X, ROBERTA_Y, 200 );
	material = new THREE.MeshLambertMaterial({
		map: THREE.ImageUtils.loadTexture('./css/img/simulator/dummy_down.png')
	}); 

	//material = new THREE.MeshBasicMaterial({color: 0x00ff00});
	
	roberta = new THREE.Mesh( geometry, material );
	roberta.overdraw = true;
	scene.add( roberta );
	
	var obgeo = new THREE.BoxGeometry( 200, 200, 200 );
	var obmat =  new THREE.MeshLambertMaterial({
									map: THREE.ImageUtils.loadTexture('http://www.html5canvastutorials.com/demos/assets/crate.jpg')
				});
	
	obstical = new THREE.Mesh( obgeo, obmat );
	obstical.overdraw = true;
	
	obstical.position.x = -400;
	obstical.position.y = 230;
	
	scene.add( obstical );
	meshArray.push( obstical );

	var stone = new THREE.MeshLambertMaterial({
		map: THREE.ImageUtils.loadTexture('http://www.html5canvastutorials.com/demos/assets/crate.jpg')
	});
	
	
	var obsticalA = new THREE.Mesh( obgeo, stone );
	obsticalA.overdraw = true;
	 
	 obsticalA.position.x = 400;
	 obsticalA.position.y = -230;
	
	scene.add( obsticalA );
	meshArray.push( obsticalA );
}
	
function moveRoberta(cmd){
		
	for(var i = 0; i <  meshArray.length; i++){
		
		switch(cmd){
		case "left":
			meshArray[i].position.x -= MOVEMENT_STEP;
			break;
		case "right":
			meshArray[i].position.x += MOVEMENT_STEP;
			break;
		case "up":
			meshArray[i].position.y -= MOVEMENT_STEP;
			break;
		case "down":
			meshArray[i].position.y += MOVEMENT_STEP;		
			break;
		}
	}

	detectCollision();
}



function detectCollision() {
	
	var robertaPosistionFrameX = roberta.position.x + ROBERTA_X;
	var robertaPosistionFrameY = roberta.position.y + ROBERTA_Y;
	
	//console.log("Roberta: x=" + robertaPosistionFrameX + " y=" + robertaPosistionFrameY);
	
	var currentMeshFrameX;
	var currentMeshFrameY;
		
	for(var meshIndex = 0; meshIndex < meshArray.length; meshIndex++){
		
		currentMeshFrameX = meshArray[meshIndex].position.x;
		currentMeshFrameY = meshArray[meshIndex].position.y;
		
		//console.log("Checking " + checkCount + " current obstical: x=" + currentMeshFrameX + " y=" + currentMeshFrameY);
		
		if(currentMeshFrameX <= robertaPosistionFrameX && currentMeshFrameX >= (robertaPosistionFrameX * (-1)) && currentMeshFrameY <= robertaPosistionFrameY && currentMeshFrameY >= (robertaPosistionFrameY * (-1))){
			console.log("Collusion Nr." + collusionCount + " detected at x=" + currentMeshFrameX + " y=" + currentMeshFrameY + " !");
			
			collisionStatus = true;

		}
	}
	
	collusionCount += 1;
	
	collisionStatus = false;
}


