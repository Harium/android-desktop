package android.content;

import android.app.SharedPreferencesImpl;

public class Context {

    /**
     * File creation mode: the default mode, where the created file can only
     * be accessed by the calling application (or all applications sharing the
     * same user ID).
     */
    public static final int MODE_PRIVATE = 0x0000;

    @Deprecated
    public static final int MODE_WORLD_READABLE = 0x0001;

    @Deprecated
    public static final int MODE_WORLD_WRITEABLE = 0x0002;

    public static SharedPreferences getSharedPreferences(String name, int mode) {
        return new SharedPreferencesImpl(name);
    }

}
