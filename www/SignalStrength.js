var exec = require('cordova/exec');
var argscheck = require('cordova/argscheck');

function SignalStrength() {}

SignalStrength.prototype.getbBm = function (successCallback, errorCallback) {
  	exec(successCallback, errorCallback, 'SignalStrength', 'getbBm', []);
};

SignalStrength.prototype.getPercentage = function (successCallback, errorCallback, options) {
	options = options || {};
    var getValue = argscheck.getValue;
    var typeNetwork = getValue(options.typeNetwork, 'wifi');
	args = [typeNetwork];
  	exec(successCallback, errorCallback, 'SignalStrength', 'getPercentage', args);
};

SignalStrength.install = function () {
  	if (!window.plugins) {
    	window.plugins = {};
  	}


  	window.plugins.signalStrength = new SignalStrength();
  	return window.plugins.signalStrength;
};

cordova.addConstructor(SignalStrength.install);
