package android.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import java.io.File;

public class Activity extends ContextThemeWrapper {

    protected void onCreate(Bundle savedInstanceState) {

    }

    public SharedPreferences getPreferences(int mode) {
        return new SharedPreferencesImpl(getLocalClassName());
    }

    public String getLocalClassName() {
        final String pkg = getPackageName();
        final String cls = getClass().getName();
        int packageLen = pkg.length();
        if (!cls.startsWith(pkg) || cls.length() <= packageLen
                || cls.charAt(packageLen) != '.') {
            return cls;
        }
        return cls.substring(packageLen+1);
    }

    private String getPackageName() {
        String pkg = System.getProperty("sun.java.command");
        int lastDot = pkg.lastIndexOf('.');
        pkg = pkg.substring(0, lastDot);
        return pkg;
    }

}
