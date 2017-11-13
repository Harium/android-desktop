package android.content.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AssetManager {
    public InputStream open(String path) throws FileNotFoundException {
        return new FileInputStream(new File(path));
    }

    public String[] list(String path) {
        return new File(path).list();
    }
}
