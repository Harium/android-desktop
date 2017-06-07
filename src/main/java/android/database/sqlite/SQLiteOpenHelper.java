package android.database.sqlite;

import android.content.Context;

import java.io.File;

public class SQLiteOpenHelper {

    private static final String JDBC_PREFIX = "jdbc:sqlite:";

    String path;

    int databaseVersion;
    String databaseName;
    private SQLiteDatabase database;

    public SQLiteOpenHelper(Context context, String databaseName, Object o, int databaseVersion) {
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;

        path = System.getProperty("user.dir");

        String url = JDBC_PREFIX + path + File.separator + databaseName;

        database = new SQLiteDatabase(url);

        if (!exists(database)) {
            database.createDatabase();
            onCreate(database);
        }
    }

    private boolean exists(SQLiteDatabase database) {
        File file = new File(database.url.substring(JDBC_PREFIX.length()));
        return file.exists();
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int fromVersion, int toVersion) {

    }

    /**
     * Get the database ready to write
     *
     * @return SQLiteDatabase
     * @throws SQLiteException
     */
    public SQLiteDatabase getWritableDatabase() throws SQLiteException {
        return database;
    }

    public SQLiteDatabase getReadableDatabase() {
        return database;
    }
}
