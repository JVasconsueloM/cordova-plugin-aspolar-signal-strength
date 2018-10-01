var exec = require('cordova/exec');

function SignalStrength() {}

SignalStrength.prototype.getInfo = function (successCallback, errorCallback) {
  exec(successCallback, errorCallback, 'SignalStrength', 'getInfo', []);
};

SignalStrength.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.signalStrength = new SignalStrength();

  return window.plugins.signalStrength;
};

cordova.addConstructor(SignalStrength.install);
