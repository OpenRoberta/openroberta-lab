chrome.app.runtime.onLaunched.addListener(function() {
  var width = 300;
  var height = 480;
  
  chrome.app.window.create('index.html', {
    innerBounds: {
      width: width,
      height: height
    },
      resizable: false
    },
    function(win) {
      win.outerBounds.setPosition(
        screen.availWidth - win.outerBounds.width / 2,
        screen.availHeight - win.outerBounds.height / 2
      );
    }
  );
});
