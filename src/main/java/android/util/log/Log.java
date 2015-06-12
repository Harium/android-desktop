package android.util.log;

public class Log {

	public static void d(String tag, String text) {
		System.out.println(tag+": "+text);
	}
	
	public static void e(String tag, String text) {
		System.err.println(tag+": "+text);
	}

}
