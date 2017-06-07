package android.os;

import java.util.HashMap;

public class Parcel {

    /**
     * Not implemented
     */
    @Deprecated
    public void writeString(String s) {

    }

    /**
     * Not implemented
     */
    @Deprecated
    public void writeInt(int i) {

    }

    /**
     * Not implemented
     */
    @Deprecated
    public int readInt() {
        return 0;
    }

    /**
     * Not implemented
     */
    @Deprecated
    public String readString() {
        return "";
    }

    public HashMap<String, Object> readHashMap(Object o) {
        return null;
    }

    public void writeMap(HashMap<String, Object> mValues) {

    }
}
