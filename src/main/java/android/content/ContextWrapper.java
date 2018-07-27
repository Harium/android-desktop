package android.content;

import android.content.pm.PackageManager;

public class ContextWrapper {

    public int checkSelfPermission(String permission) {
        return PackageManager.PERMISSION_GRANTED;
    }

}
