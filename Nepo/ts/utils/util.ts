declare var bricklyWorkspace;

export const checkMsgKey = function (msgKey) {
	if (msgKey) {
		console.warn('This message is not translated: ' + msgKey);
		return msgKey;
	} else {
		return "";
	}
}