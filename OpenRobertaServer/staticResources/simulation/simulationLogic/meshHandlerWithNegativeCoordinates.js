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

function resetNegativeCoor() {
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
}

function instanceMeshes() {

    group = new THREE.Object3D();

    //group = new THREE.Object3D();
    squareGeometry = new THREE.Geometry();
    squareGeometry.vertices.push(new THREE.Vector3(-.5, 0.5, 0.0));
    squareGeometry.vertices.push(new THREE.Vector3(.5, 0.5, 0.0));
    squareGeometry.vertices.push(new THREE.Vector3(.5, -0.5, 0.0));
    squareGeometry.vertices.push(new THREE.Vector3(-.5, -0.5, 0.0));
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

    var skyBoxGeometry = new THREE.BoxGeometry(1, 2, 1);
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
    playgroundGeometry.vertices.push(new THREE.Vector3(-6, 6, 0.0));
    playgroundGeometry.vertices.push(new THREE.Vector3(6, 6, 0.0));
    playgroundGeometry.vertices.push(new THREE.Vector3(6, -6, 0.0));
    playgroundGeometry.vertices.push(new THREE.Vector3(-6, -6, 0.0));
    playgroundGeometry.faces.push(new THREE.Face3(0, 1, 2));
    playgroundGeometry.faces.push(new THREE.Face3(0, 2, 3));

    var obstacleSphereGeo = new THREE.SphereGeometry(1, 10, 10);
    var sphereMaterial = new THREE.MeshBasicMaterial({
        color : 0x00642E,
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

    curveObject.position.set(-.4, 0, .001)

    squareMesh = new THREE.Mesh(squareGeometry, squareMaterial);
    squareMesh.position.set(0, 0.0, 0.0);

    lightSensoMesh = new THREE.Mesh(ligthSensoGeo, ligthSensoMat);
    //lightSensoMesh.position.set(.5625,0,-0.0625) ; optimal one 20 april 2015 , 17 hrs
    lightSensoMesh.position.set(0, 0, -0.0625);

    skyBox = new THREE.Mesh(skyBoxGeometry, skyBoxMaterial);
    skyBox.position.set(0, -.5, -.5); //to modify in positive values 

    bumperMesh = new THREE.Mesh(bumperGeometry, bumperMat);
    bumperMesh.position.set(.625, 0, -.2);

    playgroundMesh = new THREE.Mesh(playgroundGeometry, playMaterial);
    playgroundMesh.position.set(0, 0.0, -1.5);

    obstacleShepereMesh = new THREE.Mesh(obstacleSphereGeo, sphereMaterial);
    obstacleShepereMesh.position.set(3, 4, 0)

    octoedroMesh = new THREE.Mesh(longboxGeometry, longBoxMaterial);
    octoedroMesh.position.set(-4, -3, 0);
}
