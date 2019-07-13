package examples;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Delete this class before merge into master
 */
public class SharedPreferencesExample extends Activity {

    public static void main(String[] args) {

        String prefs = "MYPREFS";

        SharedPreferences preferences = getSharedPreferences(prefs, Context.MODE_PRIVATE);
        int a = preferences.getInt("myint", 0);
        System.out.println("myint = " + a);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("mybool", true);
        editor.putFloat("myfloat", 1.234f);
        editor.putInt("myint", 123);
        editor.putLong("mylong", 321L);
        editor.putString("myString", "myText");
        Set<String> texts = new LinkedHashSet<String>();
        texts.add("a");
        texts.add("b");
        texts.add("c");
        editor.putStringSet("myStringSet", texts);
        editor.commit();
    }

}
