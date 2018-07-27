package android.app;

import android.content.pm.PackageManager;
import android.os.Bundle;

public class Activity {

    protected void onCreate (Bundle savedInstanceState) {

    }

    public int checkSelfPermission(String permission) {
        return PackageManager.PERMISSION_GRANTED;
    }
}
