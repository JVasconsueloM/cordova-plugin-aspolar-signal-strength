function SignalStrength() {
}

SignalStrength.prototype.get = function (successCallback, errorCallback) {
  cordova.exec(successCallback, errorCallback);
};

SignalStrength.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.signalStrength = new SignalStrength();

  return window.plugins.signalStrength;
};

cordova.addConstructor(SignalStrength.install);