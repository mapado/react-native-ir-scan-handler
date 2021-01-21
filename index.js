import { NativeModules } from 'react-native';
import KeyEvent from 'react-native-keyevent';

export const setEnabled = NativeModules.RNScanHandler.setEnabled;
export const setRequestFocus = NativeModules.RNScanHandler.setRequestFocus;
export const setKeyMultipleEndKeyCode = NativeModules.RNScanHandler.setKeyMultipleEndKeyCode;
export const disableKeyMultipleEndKeyCode = NativeModules.RNScanHandler.disableKeyMultipleEndKeyCode;

export default KeyEvent;