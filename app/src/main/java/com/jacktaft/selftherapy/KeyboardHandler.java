package com.jacktaft.selftherapy;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.security.Key;


public class KeyboardHandler {
    private static boolean _isShown;
    private static Context _context;
    private static IBinder _windowToken;
    public static void showKeyboard(Context context, IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        _context = context;
        _windowToken = windowToken;
        _isShown = true;
    }
    public static void hideKeyboard() {
        if (_context != null && _windowToken != null) {
            InputMethodManager imm = (InputMethodManager) _context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(_windowToken, 0);
            _isShown = false;
        }
    }
    public static boolean isShown() {
        return _isShown;
    }
}
