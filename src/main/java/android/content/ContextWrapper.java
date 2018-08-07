package android.content;

import android.content.pm.PackageManager;

public class ContextWrapper extends Context {

    public int checkSelfPermission(String permission) {
        return PackageManager.PERMISSION_GRANTED;
    }

}
