package android.database;

public class Cursor implements AutoCloseable {

    /**
     * Not implemented
     */
    @Deprecated
    public void close() throws Exception {

    }

    /**
     * Not implemented
     */
    @Deprecated
    public void moveToFirst() {

    }

    /**
     * Not implemented
     */
    @Deprecated
    public boolean moveToNext() {
        return false;
    }

    /**
     * Not implemented
     */
    @Deprecated
    public long getLong(int i) {
        return 0;
    }

    /**
     * Not implemented
     */
    @Deprecated
    public String getString(int index) {
        return "";
    }

    /**
     * Not implemented
     */
    @Deprecated
    public int getInt(int index) {
        return 0;
    }

    /**
     * Not implemented
     */
    @Deprecated
    public double getDouble(int index) {
        return 0;
    }

    /**
     * Not implemented
     */
    @Deprecated
    public byte[] getBlob(int index) {
        return new byte[0];
    }

    /**
     * Not implemented
     */
    @Deprecated
    public boolean isNull(int column) {
        return false;
    }
}
