function removeAttributeDom(xmlDom) {
  for (var i = 0, x; x = xmlDom.childNodes[i]; i++) {
    if (x.nodeName == "#text")
      continue;
    x.removeAttribute("id");
    removeAttributeDom(x);
  }
}

function start() {
  var toolbox = document.getElementById('toolbox');
  Blockly.inject(document.getElementById('blocklyDiv'), {
    path : '../',
    toolbox : toolbox,
    realtime : false,
    realtimeOptions : {
      clientId : 'YOUR CLIENT ID GOES HERE',
      chatbox : {
        elementId : 'chatbox'
      },
      collabElementId : 'collaborators'
    }
  });
}

var setReady = function(value) {
  $('#ready').text(value);
};

var setResultOk = function() {
  $('#result').text('asExpected');
};

var setResultError = function() {
  $('#result').text('error');
};
var loadXMLString = function(xmlPath) {
  if (window.XMLHttpRequest) {
    xhttp = new XMLHttpRequest();
  } else {
    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }

  xhttp.open("GET", xmlPath, false);
  xhttp.send();

  return xhttp.responseText;
};

var DomToString = function(xmlDoc) {
  var oSerializer = new XMLSerializer();
  return oSerializer.serializeToString(xmlDoc);
};

var wrapOn = function(id, callback) {
  var wrapped = function(e) {
    setReady('running');
    callback(e);
  };
  $(id).on('click', wrapped);
};

function test(path) {
  var xmlTextSource = loadXMLString(path);
  var xmlDomSource = Blockly.Xml.textToDom(xmlTextSource);
  removeAttributeDom(xmlDomSource);
  xmlTextSource = DomToString(xmlDomSource);

  Blockly.mainWorkspace.clear();
  Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xmlDomSource);

  var xmlDomResult = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
  removeAttributeDom(xmlDomResult);
  var xmlTextResult = Blockly.Xml.domToPrettyText(xmlDomResult);
  console.log(xmlTextSource.trim());
  console.log(xmlTextResult.trim());
  if (xmlTextSource.trim() == xmlTextResult.trim())
    setResultOk();
  else
    setResultError();
  setReady('ready');
}

var test1 = function() {
  test('test_1.xml');
};

var test2 = function() {
  var xmlTextSource = loadXMLString('test_1.xml');
  var xmlDomSource = Blockly.Xml.textToDom(xmlTextSource);
  // removeAttributeDom(xmlDomSource);
  xmlTextSource = DomToString(xmlDomSource);

  Blockly.mainWorkspace.clear();
  Blockly.Xml.domToWorkspace(Blockly.mainWorkspace, xmlDomSource);

  var xmlDomResult = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
  // removeAttributeDom(xmlDomResult);
  var xmlTextResult = Blockly.Xml.domToPrettyText(xmlDomResult);
  if (xmlTextSource == xmlTextResult)
    setResultOk();
  else
    setResultError();
  setReady('ready');
};

var test3 = function() {
  test('test_3.xml');
};

var test4 = function() {
  test('test_4.xml');
};
var test5 = function() {
  test('test_5.xml');
};
var test6 = function() {
  test('test_6.xml');
};
var test7 = function() {
  test('test_7.xml');
};
var test8 = function() {
  test('test_8.xml');
};
var init = function() {
  wrapOn('#test1', test1);
  wrapOn('#test2', test2);
  wrapOn('#test3', test3);
  wrapOn('#test4', test4);
  wrapOn('#test5', test5);
  wrapOn('#test6', test6);
  wrapOn('#test7', test7);
  wrapOn('#test8', test8);
};

$(document).ready(init);
