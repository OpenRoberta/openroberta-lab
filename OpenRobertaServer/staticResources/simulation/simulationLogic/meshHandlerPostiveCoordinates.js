var squareGeometry;
var ligthSensoGeo;
var bumperGeometry;
var skyBoxGeometry;
var skyBoxMaterial;
var longboxGeometry;
var longBoxMaterial;
var squareMesh;
var triMesh;
var lightSensoMesh;
var skyBox;
var bumperMesh;
var obstacleShepereMesh;
var octoedroMesh;
var playgroundGeometry;
var group;
var curveObject;
var returnColors = [ 0, 0, 0 ];
var RED_COLOR_VECTOR = [ 247, 1, 23 ]; // as Robot June2015 Open Robert detects, there are  7 Different Colours Values
var GREEN_COLOR_VECTOR = [ 0, 100, 46 ];
var BlUE_COLOR_VECTOR = [ 0, 87, 166 ];
var BLACK_COLOR_VECTOR = [ 255, 255, 255 ];
var WHITE_COLOR_VECTOR = [ 0, 0, 0 ];
var BROWN_COLOR_VECTOR = [ 83, 33, 21 ];
var ORANGE_COLOR_VECTOR = [ 179, 0, 6 ];
var RED_BASIC_INDEX = 0;
var GREEN_BASIC_INDEX = 1;
var BLUE_BASIC_INDEX = 2;
var WHITE_TRESHOLD = 700;
var BLACK_TRESHOLD = 50;
var RED_TRESHOLD = 179;
var ULTRA_SONIC_RANGE = 170 / 14.8; // based lejos description instead  of Lego. 
var semiSphereUltraSonicMesh;
var RADIANS_CONVERTOR = Math.PI / 180;
var mappingDivideValue = 14.8;

function resetMeshHendlerPositiveCoor() {
    squareGeometry = undefined;
    ligthSensoGeo = undefined;
    bumperGeometry = undefined;
    skyBoxGeometry = undefined;
    skyBoxMaterial = undefined;
    longboxGeometry = undefined;
    longBoxMaterial = undefined;
    squareMesh = undefined;
    triMesh = undefined;
    lightSensoMesh = undefined;
    skyBox = undefined;
    bumperMesh = undefined;
    obstacleShepereMesh = undefined;
    octoedroMesh = undefined;
    playgroundGeometry = undefined;
    group = undefined;
    curveObject = undefined;
    returnColors = [ 0, 0, 0 ];
}

function instanceMeshes() {

    group = new THREE.Object3D();

    //group = new THREE.Object3D();
    squareGeometry = new THREE.Geometry();
    squareGeometry.vertices.push(new THREE.Vector3(0, 1, 0.0)); // change from -.5,  0.5, 0.0 to  0,1,0 in order to keep positive values 
    squareGeometry.vertices.push(new THREE.Vector3(1, 1, 0.0)); // change from .5,  0.5, 0.0 to  1,1,0  
    squareGeometry.vertices.push(new THREE.Vector3(1, 0, 0.0)); // change from .5, - 0.5, 0.0 to  1,0,0 
    squareGeometry.vertices.push(new THREE.Vector3(0, 0, 0.0)); // change from -.5, - 0.5, 0.0 to  0,0,0 
    squareGeometry.faces.push(new THREE.Face3(0, 1, 2));
    squareGeometry.faces.push(new THREE.Face3(0, 2, 3));

    // Light sensor Set up 
    ligthSensoGeo = new THREE.Geometry();
    // 2d vertices square floor 
    ligthSensoGeo.vertices.push(new THREE.Vector3(-.0625, .0625, -.0625)); //0
    ligthSensoGeo.vertices.push(new THREE.Vector3(0, 0, -.0625)); //1
    ligthSensoGeo.vertices.push(new THREE.Vector3(-.0625, -.0625, -.0625));//2
    ligthSensoGeo.vertices.push(new THREE.Vector3(0.0625, 0.0625, -0.0625)); //3
    ligthSensoGeo.vertices.push(new THREE.Vector3(0.0625, -0.0625, -0.0625));//4

    // adding more vertices to generate cube box
    ligthSensoGeo.vertices.push(new THREE.Vector3(-.0625, .0625, 0.0625)); //5
    ligthSensoGeo.vertices.push(new THREE.Vector3(0.0625, 0.0625, 0.0625));//6
    ligthSensoGeo.vertices.push(new THREE.Vector3(0.0625, -0.0625, 0.0625));//7
    ligthSensoGeo.vertices.push(new THREE.Vector3(-0.0625, -0.0625, 0.0625)); //8

    ligthSensoGeo.faces.push(new THREE.Face3(0, 1, 2));
    ligthSensoGeo.faces.push(new THREE.Face3(0, 1, 3));
    ligthSensoGeo.faces.push(new THREE.Face3(1, 2, 4));
    ligthSensoGeo.faces.push(new THREE.Face3(1, 3, 4));

    // adding new triangle faces which are needed for the cube box sensor
    ligthSensoGeo.faces.push(new THREE.Face3(0, 3, 5));
    ligthSensoGeo.faces.push(new THREE.Face3(3, 5, 6));
    ligthSensoGeo.faces.push(new THREE.Face3(2, 4, 7));
    ligthSensoGeo.faces.push(new THREE.Face3(2, 7, 8));

    ligthSensoGeo.faces.push(new THREE.Face3(3, 4, 7));
    ligthSensoGeo.faces.push(new THREE.Face3(3, 6, 7));

    ligthSensoGeo.faces.push(new THREE.Face3(0, 2, 8));
    ligthSensoGeo.faces.push(new THREE.Face3(0, 5, 8));

    ligthSensoGeo.faces.push(new THREE.Face3(5, 6, 7));
    ligthSensoGeo.faces.push(new THREE.Face3(5, 7, 8));

    var bumperGeometry = new THREE.Geometry();
    bumperGeometry.vertices.push(new THREE.Vector3(-.375, .625, -.2)); //0
    bumperGeometry.vertices.push(new THREE.Vector3(-.125, .625, -.2)); //1
    bumperGeometry.vertices.push(new THREE.Vector3(-.125, .5, -.2)); //2
    bumperGeometry.vertices.push(new THREE.Vector3(.125, .5, -.2)); //3
    bumperGeometry.vertices.push(new THREE.Vector3(-.125, -.5, -.2)); //4
    bumperGeometry.vertices.push(new THREE.Vector3(.125, -.5, -.2)); //5
    bumperGeometry.vertices.push(new THREE.Vector3(-.375, -.625, -.2)); //6
    bumperGeometry.vertices.push(new THREE.Vector3(-.125, -.625, -.2)); //7

    // top vertices of bumper 
    bumperGeometry.vertices.push(new THREE.Vector3(-.375, .625, .2)); //8
    bumperGeometry.vertices.push(new THREE.Vector3(-.125, .625, .2)); //9
    bumperGeometry.vertices.push(new THREE.Vector3(-.125, .5, .2)); //10
    bumperGeometry.vertices.push(new THREE.Vector3(.125, .5, .2)); //11
    bumperGeometry.vertices.push(new THREE.Vector3(-.125, -.5, .2)); //12
    bumperGeometry.vertices.push(new THREE.Vector3(.125, -.5, .2)); //13
    bumperGeometry.vertices.push(new THREE.Vector3(-.375, -.625, .2)); //14
    bumperGeometry.vertices.push(new THREE.Vector3(-.125, -.625, .2)); //15

    bumperGeometry.vertices.push(new THREE.Vector3(.125, 0, 0)); //16 centre of the front bumper
    bumperGeometry.vertices.push(new THREE.Vector3(.125, .25, 0)); //17 
    bumperGeometry.vertices.push(new THREE.Vector3(.125, -.25, 0)); //18 

    // triangles faces of bumper 
    // floor 
    bumperGeometry.faces.push(new THREE.Face3(0, 1, 2));
    bumperGeometry.faces.push(new THREE.Face3(1, 2, 3));
    bumperGeometry.faces.push(new THREE.Face3(2, 3, 4));
    bumperGeometry.faces.push(new THREE.Face3(3, 4, 5));
    bumperGeometry.faces.push(new THREE.Face3(4, 5, 7));
    bumperGeometry.faces.push(new THREE.Face3(4, 6, 7));

    //top
    bumperGeometry.faces.push(new THREE.Face3(8, 9, 10));
    bumperGeometry.faces.push(new THREE.Face3(9, 10, 11));
    bumperGeometry.faces.push(new THREE.Face3(10, 11, 12));
    bumperGeometry.faces.push(new THREE.Face3(11, 12, 13));
    bumperGeometry.faces.push(new THREE.Face3(12, 13, 15));
    bumperGeometry.faces.push(new THREE.Face3(12, 14, 15));

    //front
    bumperGeometry.faces.push(new THREE.Face3(5, 13, 18));
    bumperGeometry.faces.push(new THREE.Face3(3, 11, 17));
    bumperGeometry.faces.push(new THREE.Face3(13, 16, 18));
    bumperGeometry.faces.push(new THREE.Face3(11, 13, 16));
    bumperGeometry.faces.push(new THREE.Face3(11, 16, 17));
    bumperGeometry.faces.push(new THREE.Face3(5, 16, 18));
    bumperGeometry.faces.push(new THREE.Face3(3, 16, 17));
    bumperGeometry.faces.push(new THREE.Face3(3, 5, 16));

    //back faces
    bumperGeometry.faces.push(new THREE.Face3(0, 8, 10));
    bumperGeometry.faces.push(new THREE.Face3(0, 2, 10));
    bumperGeometry.faces.push(new THREE.Face3(2, 4, 10));
    bumperGeometry.faces.push(new THREE.Face3(4, 10, 12));
    bumperGeometry.faces.push(new THREE.Face3(4, 12, 14));
    bumperGeometry.faces.push(new THREE.Face3(4, 6, 14));

    // sides
    bumperGeometry.faces.push(new THREE.Face3(0, 1, 8));
    bumperGeometry.faces.push(new THREE.Face3(1, 3, 11));
    bumperGeometry.faces.push(new THREE.Face3(1, 8, 9));
    bumperGeometry.faces.push(new THREE.Face3(1, 9, 11));

    bumperGeometry.faces.push(new THREE.Face3(5, 7, 13));
    bumperGeometry.faces.push(new THREE.Face3(7, 13, 15));
    bumperGeometry.faces.push(new THREE.Face3(7, 14, 15));

    bumperGeometry.faces.push(new THREE.Face3(7, 6, 14));

    var skyBoxGeometry = new THREE.BoxGeometry(1, 1, 1);
    var skyBoxMaterial = new THREE.MeshBasicMaterial({
        color : 0x00FF00,
        side : THREE.DoubleSide
    });

    var longboxGeometry = new THREE.BoxGeometry(1, 1, 1);
    var longBoxMaterial = new THREE.MeshBasicMaterial({
        color : 0xF7D117,
        side : THREE.DoubleSide
    });
    /*
     * var boxMaterials = [ new THREE.MeshBasicMaterial({color:0xFF0000 ,
     * side:THREE.DoubleSide}), new THREE.MeshBasicMaterial({color:0x00FF00 ,
     * side:THREE.DoubleSide }), new THREE.MeshBasicMaterial({color:0x0000FF ,
     * side:THREE.DoubleSide}), new THREE.MeshBasicMaterial({color:0xFFFF00 ,
     * side:THREE.DoubleSide}), new THREE.MeshBasicMaterial({color:0x00FFFF ,
     * side:THREE.DoubleSide}), new THREE.MeshBasicMaterial({color:0xFFFFFF ,
     * side:THREE.DoubleSide}) ];
     */// seems no supported
    playgroundGeometry = new THREE.Geometry();
    playgroundGeometry.vertices.push(new THREE.Vector3(0, 12, 0.0)); // we should change the camera position as well with the new location of pg
    playgroundGeometry.vertices.push(new THREE.Vector3(12, 12, 0.0));
    playgroundGeometry.vertices.push(new THREE.Vector3(12, 0, 0.0));
    playgroundGeometry.vertices.push(new THREE.Vector3(0, 0, 0.0));
    playgroundGeometry.faces.push(new THREE.Face3(0, 1, 2));
    playgroundGeometry.faces.push(new THREE.Face3(0, 2, 3));

    var obstacleSphereGeo = new THREE.SphereGeometry(1, 10, 10);
    var sphereMaterial = new THREE.MeshBasicMaterial({
        color : 0x00642E,
        side : THREE.DoubleSide
    });

    // adding mesh of ultrasonic simulation
    var semiSphereGeo = new THREE.SphereGeometry(ULTRA_SONIC_RANGE, 10, 10, 0 * RADIANS_CONVERTOR, 10 * RADIANS_CONVERTOR, 0 * RADIANS_CONVERTOR,
            50 * RADIANS_CONVERTOR);
    var semiSphereMaterial = new THREE.MeshBasicMaterial({
        color : 0xF90066,
        transparent : true,
        opacity : 0.2, // keep visible just to show how it works but it should be transparent
        side : THREE.DoubleSide
    });

    var squareMaterial = new THREE.MeshBasicMaterial({
        color : 0x8080FF,
        // map:neheTexture, 
        side : THREE.DoubleSide
    });

    var triMaterial = new THREE.MeshBasicMaterial({
        color : 0xAAAAFF,

        side : THREE.DoubleSide

    });

    var ligthSensoMat = new THREE.MeshBasicMaterial({
        color : 0xFF0000,

        side : THREE.DoubleSide

    });

    var bumperMat = new THREE.MeshBasicMaterial({
        //color:0xF7D117, yellow
        color : 0xFFFFFF,

        side : THREE.DoubleSide

    });

    var playMaterial = new THREE.MeshBasicMaterial({
        color : 0xcdc9c9,

        side : THREE.DoubleSide

    });

    var curve = new THREE.CubicBezierCurve3(new THREE.Vector3(-5.5, 0, 0), new THREE.Vector3(-2, -3, 0), new THREE.Vector3(6, 9, 0), new THREE.Vector3(5,
            -5.79, 0));
    var geometry = new THREE.Geometry();
    geometry.vertices = curve.getPoints(50);
    var material = new THREE.LineBasicMaterial({
        color : 0x000000,
        linewidth : 9
    });
    curveObject = new THREE.Line(geometry, material);

    curveObject.position.set(6, 13, .001)//  change from  -.4,0,.001 to 6,&, .001, latest y = 13

    squareMesh = new THREE.Mesh(squareGeometry, squareMaterial);
    squareMesh.position.set(-.5, -.5, 1); // change from  -.5.-.5, 0 to -.5.-.5, 1

    lightSensoMesh = new THREE.Mesh(ligthSensoGeo, ligthSensoMat);
    //lightSensoMesh.position.set(.5625,0,-0.0625) ; optimal one 20 april 2015 , 17 hrs
    lightSensoMesh.position.set(0, 0, .9375);// change from 0,0,-0.0625 to 0., 0, .9375 = (1 - -0.0625)

    skyBox = new THREE.Mesh(skyBoxGeometry, skyBoxMaterial);
    skyBox.position.set(2, 1, 0); //to modify in positive values, change from  0, -.5 ,-.5 to  2,1,0

    bumperMesh = new THREE.Mesh(bumperGeometry, bumperMat);
    bumperMesh.position.set(.625, 0, .6);

    playgroundMesh = new THREE.Mesh(playgroundGeometry, playMaterial);
    playgroundMesh.position.set(0, 0.0, 0); // change from -1.5 to 0  on z axes 

    obstacleShepereMesh = new THREE.Mesh(obstacleSphereGeo, sphereMaterial);
    obstacleShepereMesh.position.set(4, 10, 1) // change form 3,4, 0 to 4, 10 , 1. 
    //The z value is one because Sphere's ratio is one as well.

    octoedroMesh = new THREE.Mesh(longboxGeometry, longBoxMaterial);
    octoedroMesh.position.set(6, 6, .5); // change form -4,-3, 0 to 5, 7 , .5.

    semiSphereUltraSonicMesh = new THREE.Mesh(semiSphereGeo, semiSphereMaterial);
    semiSphereUltraSonicMesh.position.set(0, 0, 0); // a little up from the body
    semiSphereUltraSonicMesh.rotation.z = -115 * RADIANS_CONVERTOR;
}

function getLighBFromRGB(R, G, B) {
    // it is used the approach of HSP Color Model
    var light;
    light = Math.sqrt((.299 * Math.POW(R / 255, 2)) + (.587 * Math.POW(G / 255, 2)) + (.114 * Math.POW(B / 255, 2)));
    return light;

}

function filterRGB(colorArray) {
    var coloursCounter = 0;
    for (index = 0; index < 3; index++) { // Counting the number of basic colours on the RGB
        if (colorArray[index] > 0) { // positive value
            coloursCounter++;
        }
    }

    if ((colorArray[RED_BASIC_INDEX] + colorArray[GREEN_BASIC_INDEX] + colorArray[BlUE_BASIC_INDEX]) > WHITE_TRESHOLD) {

        returnColors = WHITE_COLOR_VECTOR; // converting to White because the huge amount of colours 
    } else {
        if ((colorArray[RED_BASIC_INDEX] + colorArray[GREEN_BASIC_INDEX] + colorArray[BLUE_BASIC_INDEX]) < BLACK_TRESHOLD) {
            if (coloursCounter > 1) { // more than one colour 

                returnColors = BLACK_COLOR_VECTOR; // converting to Black
            } else {

                returnColors = BROWN_COLOR_VECTOR; // converting to Brown  Because on the list of Seven detected colours by Robot it is the lower later than Black. 
            }

        } else {
            var maxColorIndex = getMaxIndex(colorArray); // obtaining the biggest index by amount of basic colour 
            switch (maxColorIndex) {
            case RED_BASIC_INDEX:
                if (colorArray[RED_BASIC_INDEX] > RED_TRESHOLD) {

                    returnColors = RED_COLOR_VECTOR;
                } else {

                    returnColors = ORANGE_COLOR_VECTOR;
                }
                break;
            case GREEN_BASIC_INDEX:
                returnColors = GREEN_COLOR_VECTOR;
                break;
            case BLUE_BASIC_INDEX:
                returnColors = BLUE_COLOR_VECTOR;
                break;

            default:
                returnColors = GREEN_COLOR_VECTOR; // easier to read for human begin . This is just an arbitrary chose.  
                break;
            }
        }
    }
    return returnColors;
}

function getMaxIndex(valuesArray) {
    var i = 1;
    var mi = 0;
    while (i < valuesArray.length) {
        if (!(valuesArray[i] < valuesArray[mi])) {
            mi = i;
        }
        i += 1;
    }
    return mi;
}
