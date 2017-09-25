package android.util;

public class Log {

	public static void d(String tag, String text) {
		System.out.println(tag+": "+text);
	}
	
	public static void e(String tag, String text) {
		System.err.println(tag+": "+text);
	}

	public static void e(String tag, String text, Exception e) {
		System.err.println(tag+": "+text);
	}
}
