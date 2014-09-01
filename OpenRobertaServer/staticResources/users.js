var token = '1A2B3C4D';
var userAccountName = "none";
var userId = "none";
var userRole = "none";


function saveUserToServer(){
	
	var $userAccountName = $("#accountName");
	var $userName = $('#userName');
	var $userEmail = $("#userEmail");
	
	var $pass1 = $('#pass1');
	var $pass2 = $('#pass2');
	
	var $role = $("input[name=role]:checked");
		
	if($pass1.val() != $pass2.val() ){
		alert("Wrong password");
	}else if($role.val() == null){
		alert("Select your role");
	}else{
		
		COMM.json("/blocks", {
			"cmd" : "saveUser",
			"accountName" :$userAccountName.val(),
			"userName" : $userName.val(),
			"userEmail" :$userEmail.val(),
			"password" : $pass1.val(),
			"role" : $role.val()
		}, function(result) {
				if(result.created == "False")
					alert("User already exists, choose a different account name");
				else
					alert("User created!");
		});
	}
}

function deleteUserOnServer(){
	
	var $userAccountName = $("#accountNameD");
	alert("To delete "+$userAccountName.val());
	COMM.json("/blocks", {
		"cmd" : "deleteUser",
		"accountName" : $userAccountName.val()
	}, function(result){ alert(result.rc); });
	
}

function signIn(){
	
	var $userAccountName = $("#accountNameS");
	var $pass1 = $('#pass1S');
	
	COMM.json("/blocks", {
		"cmd" : "signInUser",
		"accountName" :$userAccountName.val(),
		"password" : $pass1.val(),
	}, function(response){
			 if(response.exists == "True"){
				 
				 userAccountName = response.userAccountName;
				 userId = response.userId;
				 userRole = response.userRole;
				 
				 $("#setName" ).text(response.userAccountName);
				 alert("Welcome back "+userId);
				 $( "#tutorials" ).fadeOut(700);
            	
			 }else{
				 alert("Wrong user or wrong password!");
			 }
		});
}
