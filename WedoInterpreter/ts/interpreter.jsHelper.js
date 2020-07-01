define(["require","simulation.simulation", "exports"], function(require,SIM, exports) {

	function getBlockById(id) {
		return Blockly.getMainWorkspace().getBlockById(id);
	}

	exports.getBlockById = getBlockById;

	function setSimBreak(){
		SIM.setPause(true);
	}
	exports.setSimBreak = setSimBreak;

	function getJqueryObject(object) {
		return $(object);
	}
	exports.getJqueryObject = getJqueryObject;
});