var exec = require('cordova/exec');
var argscheck = require('cordova/argscheck');

function SignalStrength() {}

SignalStrength.prototype.getdBm = function (successCallback, errorCallback) {
  	exec(successCallback, errorCallback, 'SignalStrength', 'getdBm', []);
};

SignalStrength.prototype.getPercentage = function (successCallback, errorCallback, options) {
  	options = options || {};
    var typeNetwork = options.typeNetwork || 'notWifi';
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
