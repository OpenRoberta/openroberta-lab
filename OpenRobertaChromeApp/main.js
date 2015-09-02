chrome.app.runtime.onLaunched.addListener(function() {
  var width = 280;
  var height = 355;
  
  chrome.app.window.create('index.html', {
    innerBounds: {
      width: width,
      height: height
    },
      resizable: false
    },
    function(win) {
      win.outerBounds.setPosition(
        Math.round((screen.availWidth - win.outerBounds.width) / 2),
        Math.round((screen.availHeight - win.outerBounds.height) / 2)
      );
    }
  );
});
