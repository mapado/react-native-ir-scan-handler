package com.mapado.rnscanhandler;

import android.app.Activity;
import android.view.View;
import android.os.Build;
import com.github.kevinejohn.keyevent.KeyEventModule;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import java.lang.Runnable;
import java.util.Arrays;
import android.view.KeyEvent;

public class RNScanHandlerModule extends ReactContextBaseJavaModule {
    private static RNScanHandlerModule instance = null;
    private final ReactApplicationContext reactContext;

    private boolean enabled = false;
    private boolean requestFocus = false;
    private Integer keyMultipleEndKeyCode = null;

    public RNScanHandlerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    public static RNScanHandlerModule getInstance() {
        return instance;
    }

    public static RNScanHandlerModule initRNScanHandlerModule(ReactApplicationContext reactContext) {
        instance = new RNScanHandlerModule(reactContext);
        return instance;
    }

    @ReactMethod
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @ReactMethod
    public void setRequestFocus(boolean requestFocus) {
        this.requestFocus = requestFocus;
    }

    public void setKeyMultipleEndKeyCode(int keyCode) {
        this.keyMultipleEndKeyCode = keyCode;
    }

    public void dispatchKeyEvent(KeyEvent event) {
        if (!this.enabled) {
            return;
        }

        // the following is used to get the barcodeData from the barcode reader on some devices
        // when their keys are pushed.
        // If any touchable input has focus then call clearFocus otherwise KeyEvent.ACTION_MULTIPLE will
        // not trigger this method and the app won't be able to read the data
        if (this.requestFocus) {
            doRequestFocus();
        }

        // KeyEvent.ACTION_MULTIPLE = 2
        // KeyEvent.KEYCODE_UNKNOWN = 0
        // KeyEvent.ACTION_DOWN = 0
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE && (this.keyMultipleEndKeyCode == null || event.getKeyCode() == this.keyMultipleEndKeyCode)) {
            KeyEventModule.getInstance().onKeyMultipleEvent(event.getKeyCode(), event.getRepeatCount(), event);
        } else if (event.getAction() == KeyEvent.ACTION_DOWN) {
            KeyEventModule.getInstance().onKeyDownEvent(event.getKeyCode(), event);
        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            KeyEventModule.getInstance().onKeyUpEvent(event.getKeyCode(), event);
        }
    }

    @Override
    public String getName() {
        return "RNScanHandler";
    }

    private void doRequestFocus() {
        final Activity currentActivity = reactContext.getCurrentActivity();
            currentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    View focusedView = currentActivity.getCurrentFocus();

                    if (focusedView != null) {
                        // remove focus from focused view
                        focusedView.clearFocus();
                    }

                    View contentView = currentActivity.getWindow().getDecorView().findViewById(android.R.id.content);
                    // in any case, request focus from touch
                    contentView.requestFocusFromTouch();
                }
            });
    }
}