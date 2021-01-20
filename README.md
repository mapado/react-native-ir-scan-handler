# React Native Scan Handler

## What is it for ?

This package allows handling standard IR scanners input on android in a React Native app.
The IR scanner should be set to send either KeyDown, KeyUp or KeyMultiple events. It depends on react-native-keyevent, therefore, you should follow android installation instructions here: https://github.com/kevinejohn/react-native-keyevent

## Installation

```
yarn add react-native-ir-scan-handler
```

### Configuration

#### Android

Implement onConfigurationChanged method in MainActivity.java

```java
    ......
    import com.mapado.rnscanhandler.RNScanHandlerModule; // <--- import
    ......

    public class MainActivity extends ReactActivity {
      ......
      @Override
      public boolean dispatchKeyEvent(KeyEvent event) {
          RNScanHandlerModule.getInstance().dispatchKeyEvent(event);
          return super.dispatchKeyEvent(event);
      }
      ......
    }
```

## Usage

```javascript
import React, { PureComponent } from 'react';
import { View } from 'react-native';
import KeyEvent, {
  setEnabled,
  setRequestFocus,
  setKeyMultipleEndKeyCode,
} from 'react-native-scan-handler';

class ScanHandler extends PureComponent {
  componentDidMount() {
    setEnabled(true); // enables the module
    setRequestFocus(true); // some devices requires that android requests the touch focus to be able to catch key events
    setKeyMultipleEndKeyCode(0); // set the ending character of the onKeyMultiple sequence (list of android KeyCodes https://developer.android.com/reference/android/view/KeyEvent)

    // if you want to react to keyUp
    KeyEvent.onKeyUpListener((keyEvent) => {
      console.log(`onKeyUp keyCode: ${keyEvent.keyCode}`);
      console.log(`Action: ${keyEvent.action}`);
      console.log(`Key: ${keyEvent.pressedKey}`);
      console.log(`Character: ${keyEvent.character}`); // android only
    });

    // if you want to react to keyDown
    KeyEvent.onKeyDownListener((keyEvent) => {
      console.log(`onKeyDown keyCode: ${keyEvent.keyCode}`);
      console.log(`Action: ${keyEvent.action}`);
      console.log(`Key: ${keyEvent.pressedKey}`);
      console.log(`Character: ${keyEvent.character}`); // android only
    });

    // if you want to react to keyMultiple
    KeyEvent.onKeyMultipleListener((keyEvent) => {
      console.log(`onKeyMultiple keyCode: ${keyEvent.keyCode}`);
      console.log(`Action: ${keyEvent.action}`);
      console.log(`Characters: ${keyEvent.characters}`); // android only
    });
  }

  componentWillUnmount() {
    setEnabled(false); // disables the module
    setRequestFocus(false);
    setKeyMultipleEndKeyCode(null);

     // if you are listening to keyUp
    KeyEvent.removeKeyUpListener();

     // if you are listening to keyDown
    KeyEvent.removeKeyDownListener();

     // if you are listening to keyMultiple
    KeyEvent.removeKeyMultipleListener();
  }
}

```