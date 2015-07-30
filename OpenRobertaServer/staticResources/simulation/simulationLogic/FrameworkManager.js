/**
 * Initialze the scene.
 */
function initializeScene() {

    resetMeshHendlerPositiveCoor();
    resetMotionHandler();
    resetGeneratorRoberta();

    // if(Detector.webgl){ 
    //   renderer = new THREE.WebGLRenderer({antialias:true}); 

    // If its not supported, instantiate the canvas renderer to support all non WebGL 
    // browsers 
    //} else { 
    renderer = new THREE.CanvasRenderer(); // WE agreed of keep as DEFAULT Canvas  for Open Roberta easy access  
    //} 

    // Set the background color of the renderer to black, with full opacity 
    renderer.setClearColor(0x000000, 1);

    // Get the size of the inner window (content area) to create a full size renderer 
    canvasWidth = window.innerWidth;
    canvasHeight = window.innerHeight;

    // Set the renderers size to the content areas size 
    renderer.setSize(canvasWidth, canvasHeight);

    // Get the DIV element from the HTML document by its ID and append the renderers DOM 
    // object to it 
    document.getElementById("WebGLCanvas").appendChild(renderer.domElement);

    // Create the scene, in which all objects are stored (e. g. camera, lights, 
    // geometries, ...) 
    scene = new THREE.Scene();

    // taking cube example the new camera configuration is settled 
    camera = new THREE.PerspectiveCamera(30, window.innerWidth / window.innerHeight, 1, 1000);
    camera.position.y = 6;
    camera.position.x = 6;
    camera.position.z = 50; // for testing reasons the camera is change to  50 instead 30 
    //camera.rotation.y = .45 ;	
    scene.add(camera);

    instanceMeshes(); // calling for meshHandler a method which instances the meshes with their materials and properties.
    group.position.set(-15, 6, .3); // change from -3, 1, -.5 to 0,6,0 to keep positive values 

    var cloneCube = octoedroMesh.clone();
    cloneCube.position.set(3, 5.5, .5);// change from 2, -1.5, 1 to 8,5.5,.5to keep positive values

    playgroundMesh.add(curveObject);
    playgroundMesh.add(obstacleShepereMesh);
    playgroundMesh.add(octoedroMesh);
    //playgroundMesh.add(cloneCube); This time it is not required 

    scene.add(playgroundMesh);

    scene.add(skyBox);

    group.add(lightSensoMesh);
    group.add(squareMesh);
    if (statusUltraSonicSensor) {
        group.add(semiSphereUltraSonicMesh);
    }
    group.add(bumperMesh);
    // provisional ultrasonic mesh

    // first the group should be added on the scene
    scene.add(group);

    // raycasting settings
    // collidable list objects 	
    collidableMeshList.push(obstacleShepereMesh);
    collidableMeshList.push(skyBox);
    //collidableMeshList.push(cloneCube); This example does not need this figure 
    collidableMeshList.push(octoedroMesh);

    colorReadableMeshList.push(curveObject);
    colorReadableMeshList.push(playgroundMesh);

    // fps counter stats variables
    // create the Stats element and append it to the Dome
    stats = new Stats();
    stats.domElement.style.position = 'absolute';
    stats.domElement.style.top = '0px';
    document.getElementById("WebGLCanvas").appendChild(stats.domElement);
    var divElement = document.createElement("div") ;
    
    divElement.style.position= 'absolute' ;
    divElement.style.top = '0px' ;
    divElement.style.left = '200px' ;
    divElement.style.color = 'white' ;
    var labelRENode = document.createTextNode(" right encoder: ") ;
     rEValueNode = document.createTextNode("0") ;
    
    var labelLENode = document.createTextNode(" left encoder: ") ;
     lEValueNode = document.createTextNode("0.0") ;
     
    var labelDisValNode = document.createTextNode(" Ultrasonic Distance: ") ;
    distanceValueUlS = document.createTextNode("0.0") ; 
    
    var labelColrValNode = document.createTextNode(" RGB Fould Value: ") ;
    foundColorValue = document.createTextNode("0.0") ; 
    
    //labelLENode.style.left = '50px' ;
    //divTest.data = 'ulala' ;
    divElement.appendChild(labelRENode) ;
    divElement.appendChild(rEValueNode) ;
    divElement.appendChild(labelLENode) ;
    divElement.appendChild(lEValueNode) ;
    divElement.appendChild(labelDisValNode) ;
    divElement.appendChild(distanceValueUlS) ;
    //divElement.appendChild(labelColrValNode) ;
    //divElement.appendChild(foundColorValue) ;
    document.getElementById("WebGLCanvas").appendChild(divElement);
    document.getElementById("WebGLCanvas").appendChild(stats.domElement);
    
    
    // making new instance of Clock object	
    clock = new THREE.Clock();

    // adding frame handler
    animationFrame = new AnimationFrame(AVG_FRAME_Rate);
}

/**
 * Animate the scene and call rendering.
 */
function animateScene() { // adding time t 26mai

    if (!PROGRAM_SIMULATION.isTerminated()) {
        step();
        updateScene(ACTORS.getLeftMotor().getPower(), ACTORS.getRightMotor().getPower());
        ACTORS.calculateCoveredDistance();
    }
}

function updateScene(motorL, motorR) {
    //time = clock.getElapsedTime(); just for debugging 
    //delta = clock.getDelta() ;
    //console.log( "time" + time) ;
    if (ACTORS.isResetTachoSensor()) {
        resetWheelRotationCounter();
        ACTORS.setResetTachoSensor(false);
    }
    renderScene();

    var positionW = new THREE.Vector3();
    positionW.setFromMatrixPosition(lightSensoMesh.matrixWorld);
    // raytracing section
    for (floorVertexIndex = 0; floorVertexIndex < 4; floorVertexIndex++) {
        var localVertex = lightSensoMesh.geometry.vertices[floorVertexIndex].clone();
        var globalVertex = localVertex.applyMatrix4(lightSensoMesh.matrix);
        var directionVector = globalVertex.sub(lightSensoMesh.position);
        var ray = new THREE.Raycaster(positionW, directionVector.clone(), 0, 3);// just one unit magnitude
        var collisionResults = ray.intersectObjects(colorReadableMeshList);

        if (collisionResults.length > 0) {
            // HERE THE READING OF LIGHT SENSOR HAPPENS 
            inpoutValuesRobot[LIGHT_COLOR_FLAG_INDEX] = true;
            inpoutValuesRobot[LIGHT_COLOR_INDEX] = "" + decimal2Hex(collisionResults[0].object.material.color.r, 2)
                    + decimal2Hex(collisionResults[0].object.material.color.g, 2) + decimal2Hex(collisionResults[0].object.material.color.b, 2);
            //collisionResults[0].object.material.color.setRGB(Math.random(),Math.random(),Math.random() );
            console.log(filterRGB(inpoutValuesRobot[LIGHT_COLOR_INDEX]));
            SENSORS.setColor(COLOR_ENUM.NONE);
            SENSORS.setLight(100);

        }
    }

    var positionBump = new THREE.Vector3();
    positionBump.setFromMatrixPosition(bumperMesh.matrixWorld);
    for (VertexIndex = 0; VertexIndex < bumperMesh.geometry.vertices.length; VertexIndex++) {
        var localVertexB = bumperMesh.geometry.vertices[VertexIndex].clone();
        var globalVertexB = localVertexB.applyMatrix4(bumperMesh.matrix);
        var directionVectorB = globalVertexB.sub(bumperMesh.position);
        var rayB = new THREE.Raycaster(positionBump, directionVectorB.clone().normalize());// just one unit magnitude
        var collisionResultsB = rayB.intersectObjects(collidableMeshList);
        if (collisionResultsB.length > 0 && collisionResultsB[0].distance < directionVectorB.length()) {
            // HERE THE COLLISION HAPPENS AND TODO PLACE
            //var oldColor = collisionResults[0].object.material.color ;
            collisionResultsB[0].object.material.color.setRGB(Math.random(), Math.random(), Math.random());
            //above changes randomize the color of touched object
            SENSORS.setTouchSensor(true);
        } else {
            SENSORS.setTouchSensor(false);
        }
    }

    if (statusUltraSonicSensor) {
        // Ultrasonic implementation, it should change to a function because the use of recurrent 3 ray-casters.
        var positionUltraSensor = new THREE.Vector3();
        positionUltraSensor.setFromMatrixPosition(semiSphereUltraSonicMesh.matrixWorld);
        var echoeObjects = [];
        var distanceArray = [];
        var minIndex;
        var echoCounter = 0;
        var nearestObject;
        for (VertexIndex = 0; VertexIndex < semiSphereUltraSonicMesh.geometry.vertices.length; VertexIndex++) {
            var localVertexUltra = semiSphereUltraSonicMesh.geometry.vertices[VertexIndex].clone();
            var globalVertexUltra = localVertexUltra.applyMatrix4(semiSphereUltraSonicMesh.matrix);
            var directionVectorUltra = globalVertexUltra.sub(semiSphereUltraSonicMesh.position);
            var rayUltra = new THREE.Raycaster(positionUltraSensor, directionVectorUltra.clone().normalize());
            var collisionResultsUltra = rayUltra.intersectObjects(collidableMeshList);
            if (collisionResultsUltra.length > 0 && collisionResultsUltra[0].distance < directionVectorUltra.length()) {

                echoeObjects[echoCounter] = collisionResultsUltra[0];
                distanceArray[echoCounter] = collisionResultsUltra[0].distance;
                echoCounter++;

            } else {

            }
        }
        if (echoCounter > 0) {
            minIndex = Math.min.apply(null, distanceArray); // sort by short distance
            minIndex = distanceArray.indexOf(minIndex);
            nearestObject = echoeObjects[minIndex];
            nearestObject.object.material.color.setRGB(Math.random(), Math.random(), Math.random()); // highlighting of closest object form ultrasonic view 
            echoeDistance = nearestObject.distance * mappingDivideValue;
            SENSORS.setUltrasonicSensor(echoeDistance);
            distanceValueUlS.data = '' + echoeDistance ;
            //console.log("distance in cm " + echoeDistance);// to check mapping result distanceS
        } else {
        	 distanceValueUlS.data = '' +255 ;
            SENSORS.setUltrasonicSensor(Infinity);
        }
    }

    gatherInputData();

    var tacho = transformBrick(getRobotMotion(motorL, motorR));
    ACTORS.getLeftMotor().setCurrentRotations(tacho[0]);
    ACTORS.getRightMotor().setCurrentRotations(tacho[1]);

    //requestAnimationFrame(animateScene); //oldest Request
    // testing frame handler
    requestId = animationFrame.request(animateScene);
    //console.log();
    // Map the 3D scene down to the 2D screen (render the frame) 
    //renderScene(); 
    stats.update();

    //  }

}

/**
 * Render the scene. Map the 3D world to the 2D screen.
 */
function renderScene() {
    renderer.render(scene, camera);
}

function transformBrick(valuesBrick) {
    //inpoutValuesRobot = valuesBrick;
    //group.position.x += valuesBrick[POSITION_X_INDEX] ;
    group.position.x += valuesBrick[DELTA_X_INDEX];
    //group.position.y += valuesBrick[POSITION_Y_INDEX] ;
    group.position.y += valuesBrick[DELTA_Y_INDEX];
    calculateWheelEncoders();
    rEValueNode.data = '' + getRightWheelRotationCounter();
    lEValueNode.data = '' + getLeftWheelRotationCounter();
    //console.log("right wheel rotation " + getRightWheelRotationCounter());// to check encode
    //console.log("left wheel rotation " + getLeftWheelRotationCounter());// to check encode
    if (valuesBrick[THETA_INDEX] != 0) { // change from ROTATION_Z_INDEX to THETA_INDEX because it comes from robotMotionHandler instead GeneratorRoberta

        group.rotation.z = valuesBrick[THETA_INDEX];
        //group.rotation.z += valuesBrick[ROTATION_Z_INDEX] ;
    } else {
        group.rotation.z = 0;

    }

    inpoutValuesRobot[LIGHT_COLOR_FLAG_INDEX] = valuesBrick[LIGHT_COLOR_FLAG_INDEX];
    inpoutValuesRobot[LIGHT_COLOR_INDEX] = valuesBrick[LIGHT_COLOR_INDEX];
    inpoutValuesRobot[POSITION_X_INDEX] = group.position.x;
    inpoutValuesRobot[POSITION_Y_INDEX] = group.position.y;
    inpoutValuesRobot[COLLISION_FLAG_INDEX] = true;

    inpoutValuesRobot[ROTATION_Z_INDEX] = group.rotation.z;
    return [ getLeftWheelRotationCounter(), getRightWheelRotationCounter() ];
}

function gatherInputData() {
    inpoutValuesRobot[POSITION_X_INDEX] = group.position.x;
    inpoutValuesRobot[POSITION_Y_INDEX] = group.position.y;
    inpoutValuesRobot[COLLISION_FLAG_INDEX] = true;

    inpoutValuesRobot[ROTATION_Z_INDEX] = group.rotation.z;

}

function decimal2Hex(d, padding) {
    var hex = Number(d).toString(16);
    padding = typeof (padding) === "undefined" || padding === null ? padding = 2 : padding;

    while (hex.length < padding) {
        hex = "0" + hex;
    }

    return hex;
}

function decimalToHex(d) {
    var hex = Number(d).toString(16);
    hex = "000000".substr(0, 6 - hex.length) + hex;
    return hex;
}
