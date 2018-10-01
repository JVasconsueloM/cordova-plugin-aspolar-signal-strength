var exec = require('cordova/exec');

function SignalStrength() {}

SignalStrength.prototype.getInfo = function (successCallback, errorCallback) {
  exec(successCallback, errorCallback, 'SignalStrength', 'getInfo', []);
};

SignalStrength.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.SignalStrength = new SignalStrength();

  return window.plugins.SignalStrength;
};

cordova.addConstructor(SignalStrength.install);
