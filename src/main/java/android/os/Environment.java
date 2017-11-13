package android.os;

import java.io.File;

public class Environment {

    /**
     * Storage state if the media is present and mounted at its mount point with
     * read/write access.
     *
     * @see #getExternalStorageState(File)
     */
    public static final String MEDIA_MOUNTED = "mounted";

    /**
     * Storage state if the media is present but not mounted.
     *
     * @see #getExternalStorageState(File)
     */
    public static final String MEDIA_UNMOUNTED = "unmounted";

    public static String getExternalStorageState(File file) {
        if(file.exists()) {
            return MEDIA_MOUNTED;
        } else {
            return MEDIA_UNMOUNTED;
        }
    }

    public static String getExternalStorageState() {
        return MEDIA_MOUNTED;
    }

    public static File getExternalStorageDirectory() {
        return new File(System.getProperty("user.dir"));
    }

    public static File getDataDirectory() {
        return getExternalStorageDirectory();
    }
}
