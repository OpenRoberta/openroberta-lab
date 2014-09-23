$(document).ready(function() {
  // POPUP SECTION BEGIN
  // Standard Pop-Up settings, can be overwritten with unique HTML IDs - see
  // below
  $(".jquerypopup").dialog({
    autoOpen : false,
    draggable : false,
    resizable : false,
    width : 400,
    show : {
      effect : 'fade',
      duration : 300
    },
    hide : {
      effect : 'fade',
      duration : 300
    }
  });

  $(".jquerypopup .submit").on('click', function() {
    $(this).closest(".jquerypopup").dialog('close');
  });

  // overwritten Styles for unique Pop-Up
  $("#delete-user").dialog({
    width : 300
  });

  // define general class for Pop-Up
  $(".jquerypopup").dialog("option", "dialogClass", "jquerypopup");

  // Close all previous Pop-Ups.
  $(".popup-opener").click(function() {
    $(".ui-dialog-content").dialog("close");
  });

  // ###############################
  // ### Openers for unique Pop-Ups
  // ###############################

  // Open Pop-Up "Login User"
  $("#open-login-user").click(function() {
    $("#login-user").dialog("open");
    altert("Hello");
  });

  // Open Pop-Up "Register User"
  $("#open-register-user").click(function() {
    $("#register-user").dialog("open");
  });

  // Open Pop-Up "Delete User"
  $("#open-delete-user").click(function() {
    $("#delete-user").dialog("open");
  });

  // Open Pop-Up "Programm"
  $("#open-program").click(function() {
    $("#program").dialog("open");
  });
  // POPUP SECTION END
});