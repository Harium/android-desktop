package android.view;

import android.content.ContextWrapper;
import android.content.res.AssetManager;

public class ContextThemeWrapper extends ContextWrapper {

    AssetManager assetManager = new AssetManager();

    public AssetManager getAssets() {
        return assetManager;
    }

}
