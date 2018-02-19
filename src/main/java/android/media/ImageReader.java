package android.media;

import android.graphics.ImageFormat;

public class ImageReader {

    int width;
    int height;
    private int imageFormat;

    public ImageReader() {
        width = 640;
        height = 480;
        imageFormat = ImageFormat.YUV_420_888;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getImageFormat() {
        return imageFormat;
    }

    public Image acquireNextImage() {
        return null;
    }
}
