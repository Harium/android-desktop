package android.util;

public class TypeHelper {
    public static boolean isBaseTypeOrArray(Object obj) {
        Class<?> c = obj.getClass();
        return c.equals(Integer.class) || c.equals(Integer[].class) ||
                c.equals(Double.class) || c.equals(Double[].class) ||
                c.equals(Float.class) || c.equals(Float[].class) ||
                c.equals(Short.class) || c.equals(Short[].class) ||
                c.equals(String.class) || c.equals(String[].class);
    }

    public static boolean isBaseTypeOrArray(byte obj) {
        return true;
    }

    public static boolean isBaseTypeOrArray(short obj) {
        return true;
    }

    public static boolean isBaseTypeOrArray(char obj) {
        return true;
    }

    public static boolean isBaseTypeOrArray(int obj) {
        return true;
    }

    public static boolean isBaseTypeOrArray(long obj) {
        return true;
    }

    public static boolean isBaseTypeOrArray(float obj) {
        return true;
    }

    public static boolean isBaseTypeOrArray(double obj) {
        return true;
    }

    public static boolean isBaseTypeOrArray(boolean obj) {
        return true;
    }
}
