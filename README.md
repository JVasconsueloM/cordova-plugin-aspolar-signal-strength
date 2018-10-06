## for use this plugin with Ionic 3
### Installation
1. Install the Cordova plugin:
    > cordova plugin add https://github.com/JVasconsueloM/aspolar-signal-strength.git

2. Build the ionic wrapper
    Open terminal and write:
    > ionic generate provider signal-strength

    Then copy and paste this on file created.

    ```typescript
    import { Injectable } from '@angular/core';
    import { Cordova, Plugin, IonicNativePlugin } from "@ionic-native/core";

    /*
      Generated class for the SignalStengthProvider provider.

      See https://angular.io/guide/dependency-injection for more info on providers
      and Angular DI.
    */
    @Plugin({
      pluginName: 'SignalStrength',
      plugin: 'cordova-aspolar-plugin-signal-strength',
      pluginRef: 'window.plugins.signalStrength',
      repo: 'https://github.com/JVasconsueloM/aspolar-signal-strength',
      platforms: ['Android']
    })

    @Injectable()
    export class SignalStrengthProvider extends IonicNativePlugin  {

      @Cordova()
      getdBm(): Promise<any> {
        return; // We add return; here to avoid any IDE / Compiler errors
      }

      @Cordova()
      getPercentage(): Promise<any> {
        return; // We add return; here to avoid any IDE / Compiler errors
      }

      @Cordova()
      getLevel(): Promise<any> {
        return; // We add return; here to avoid any IDE / Compiler errors
      }
    }

    ```
3. Add the plugin to Your App's Module
    ```typescript
    ...

    import { SignalStrengthProvider } from "../providers/signal-strength/signal-strength";

    ...

    @NgModule({
      ...

      providers: [
        ...
        SignalStrengthProvider
        ...
      ]
      ...
    })
    export class AppModule { }

    ```

4. for use... 
    ```typescript
    ...
    import { SignalStrengthProvider } from "../providers/signal-strength/signal-strength";
    ...

    export class HomePage{
      ...
      constructor(public navCtrl: NavController, private signalStrength: SignalStrengthProvider) {}
      ...
      getSignal() {
        this.signalStrength.getInfo().then((result) => {
          alert(result);
        }, (err) => {
          alert(JSON.stringify(err));
        });
      }
      ...
    }
    ```




