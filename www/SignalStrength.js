var exec = require('cordova/exec');
var argscheck = require('cordova/argscheck');

function SignalStrength() {}

// Get the signal strength as dBm
SignalStrength.prototype.getdBm = function (successCallback, errorCallback) {
  	exec(successCallback, errorCallback, 'SignalStrength', 'getdBm', []);
};

// Get the signal strength as percentage of signal 0%... 100%
SignalStrength.prototype.getPercentage = function (successCallback, errorCallback, options) {
  	options = options || {};
    var typeNetwork = options.typeNetwork || 'notWifi';
  	args = [typeNetwork];
    exec(successCallback, errorCallback, 'SignalStrength', 'getPercentage', args);
};

// Get signal level from 0..4
SignalStrength.prototype.getLevel = function (successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'SignalStrength', 'getLevel', []);
};

SignalStrength.install = function () {
  	if (!window.plugins) {
    	window.plugins = {};
  	}


  	window.plugins.signalStrength = new SignalStrength();
  	return window.plugins.signalStrength;
};

cordova.addConstructor(SignalStrength.install);
