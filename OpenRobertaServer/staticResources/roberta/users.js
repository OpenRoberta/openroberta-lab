var userState = {
    id : false,
    name : 'none',
    role : 'none',
    program : 'none',
    programSaved : false,
    brickSaved : false,
    robot : 'none',
    brickConnection : 'none',
    toolbox : 'none',
    token : '1A2B3C4D',
};

function saveUserToServer() {
    var $userAccountName = $("#accountName");
    var $userName = $('#userName');
    var $userEmail = $("#userEmail");
    var $pass1 = $('#pass1');
    var $pass2 = $('#pass2');
    var $role = $("input[name=role]:checked");

    if ($pass1.val() != $pass2.val()) {
        alert("Wrong password");
    } else if ($role.val() == null) {
        alert("Select your role");
    } else {
        var roleGerman = $role.val();
        var role = "STUDENT";
        if (roleGerman === "Lehrer") {
            role = "TEACHER";
        }
        COMM.json("/blocks", {
            "cmd" : "createUser",
            "accountName" : $userAccountName.val(),
            "userName" : $userName.val(),
            "userEmail" : $userEmail.val(),
            "password" : $pass1.val(),
            "role" : role
        }, function(result) {
            if (result.rc === "ok")
                alert("User created!");
            else
                alert("User already exists, choose a different account name");
        });
    }
}

function deleteUserOnServer() {
    var $userAccountName = $("#accountNameD");
    alert("To delete " + $userAccountName.val());
    userState.id = null;
    COMM.json("/blocks", {
        "cmd" : "deleteUser",
        "accountName" : $userAccountName.val()
    }, function(result) {
        alert(result.rc);
    });
}

function signIn() {
    var $userAccountName = $("#accountNameS");
    var $pass1 = $('#pass1S');

    COMM.json("/blocks", {
        "cmd" : "login",
        "accountName" : $userAccountName.val(),
        "password" : $pass1.val(),
    }, function(response) {
        if (response.rc === "ok") {
            userState.name = response.userAccountName;
            userState.id = response.userId;
            userState.role = response.userRole;
            displayStatus();
            setHeadNavigationMenuState('login');
            $("#tutorials").fadeOut(700);
        } else {
            alert("Wrong user or wrong password!");
        }
    });
}
