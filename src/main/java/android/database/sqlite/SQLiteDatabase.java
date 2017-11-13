package android.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.sql.*;
import java.util.*;

public class SQLiteDatabase {
    private static final String TAG = "SQLiteDatabase";

    String url;

    /**
     * When a constraint violation occurs, an immediate ROLLBACK occurs,
     * thus ending the current transaction, and the command aborts with a
     * return code of SQLITE_CONSTRAINT. If no transaction is active
     * (other than the implied transaction that is created on every command)
     * then this algorithm works the same as ABORT.
     */
    public static final int CONFLICT_ROLLBACK = 1;

    /**
     * When a constraint violation occurs,no ROLLBACK is executed
     * so changes from prior commands within the same transaction
     * are preserved. This is the default behavior.
     */
    public static final int CONFLICT_ABORT = 2;

    /**
     * When a constraint violation occurs, the command aborts with a return
     * code SQLITE_CONSTRAINT. But any changes to the database that
     * the command made prior to encountering the constraint violation
     * are preserved and are not backed out.
     */
    public static final int CONFLICT_FAIL = 3;

    /**
     * When a constraint violation occurs, the one row that contains
     * the constraint violation is not inserted or changed.
     * But the command continues executing normally. Other rows before and
     * after the row that contained the constraint violation continue to be
     * inserted or updated normally. No error is returned.
     */
    public static final int CONFLICT_IGNORE = 4;

    /**
     * When a UNIQUE constraint violation occurs, the pre-existing rows that
     * are causing the constraint violation are removed prior to inserting
     * or updating the current row. Thus the insert or update always occurs.
     * The command continues executing normally. No error is returned.
     * If a NOT NULL constraint violation occurs, the NULL value is replaced
     * by the default value for that column. If the column has no default
     * value, then the ABORT algorithm is used. If a CHECK constraint
     * violation occurs then the IGNORE algorithm is used. When this conflict
     * resolution strategy deletes rows in order to satisfy a constraint,
     * it does not invoke delete triggers on those rows.
     * This behavior might change in a future release.
     */
    public static final int CONFLICT_REPLACE = 5;

    /**
     * Use the following when no conflict action is specified.
     */
    public static final int CONFLICT_NONE = 0;

    public SQLiteDatabase(String url) {
        this.url = url;
    }

    /*
     * Not Implemented
     */
    @Deprecated
    public Cursor rawQuery(String query, String[] whereArgs) {
        return null;
    }

    /**
     * Executes SQL Query
     *
     * @param query
     */
    public void execSQL(String query) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public long insertOrThrow(String table, String nullColumnHack, ContentValues values) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(table);
        builder.append("(");

        StringBuilder valuesBuilder = new StringBuilder();
        Set<Map.Entry<String, Object>> keys = values.valueSet();

        List<Object> params = new ArrayList<>();

        int size = keys.size() - 1;
        int count = 0;
        for (Map.Entry<String, Object> entry : keys) {
            builder.append(entry.getKey());
            valuesBuilder.append("?");
            params.add(entry.getValue());

            if (count < size) {
                builder.append(",");
                valuesBuilder.append(",");
            }
            count++;
        }

        builder.append(") VALUES (");
        builder.append(valuesBuilder.toString());
        builder.append(")");

        String sql = builder.toString();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(url);
            pstmt = conn.prepareStatement(sql);
            addValues(pstmt, params);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Create operation failed, no rows affected.");
            }

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    private void addValues(PreparedStatement pstmt, List<Object> params) throws SQLException {
        int index = 1;
        for (Object param : params) {

            if (param == null) {
                index++;
                continue;
            }

            if (param.getClass().equals(int.class)) {
                pstmt.setInt(index, (Integer) param);
            } else if (param.getClass().equals(Integer.class)) {
                pstmt.setInt(index, (Integer) param);
            } else if (param.getClass().equals(short.class)) {
                pstmt.setShort(index, (short) param);
            } else if (param.getClass().equals(Short.class)) {
                pstmt.setShort(index, (Short) param);
            } else if (param.getClass().equals(long.class)) {
                pstmt.setLong(index, (long) param);
            } else if (param.getClass().equals(Long.class)) {
                pstmt.setLong(index, (Long) param);
            } else if (param.getClass().equals(double.class)) {
                pstmt.setDouble(index, (double) param);
            } else if (param.getClass().equals(Double.class)) {
                pstmt.setDouble(index, (double) param);
            } else if (param.getClass().equals(boolean.class)) {
                pstmt.setBoolean(index, (boolean) param);
            } else if (param.getClass().equals(Boolean.class)) {
                pstmt.setBoolean(index, (boolean) param);
            } else if (param.getClass().equals(Float.class)) {
                pstmt.setFloat(index, (Float) param);
            } else if (param.getClass().equals(String.class)) {
                pstmt.setString(index, (String) param);
            } else if (param.getClass().equals(Byte.class)) {
                pstmt.setByte(index, (byte) param);
            } else if (param.getClass().equals(byte[].class)) {
                pstmt.setBytes(index, (byte[]) param);
            } else {
                System.err.println("Implement the type: " + param.getClass());
            }

            index++;
        }

    }

    public int update(String tableName, ContentValues contentValues, String query, String[] params) {
        return 0;
    }

    /*
     * Not implemented
     */
    @Deprecated
    public int delete(String tableName, String query, String[] params) {
        String sql = "DROP TABLE " + tableName;

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /*
     * Not implemented
     */
    @Deprecated
    public Cursor query(String tableName, String[] projection, String where, String[] whereArgs, Object o, Object o1, String orderBy1, Object o2) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        builder.append(projectionToQuery(projection));
        builder.append(" FROM ");
        builder.append(tableName);

        builder.append(whereToQuery(where, whereArgs));

        String query = builder.toString();

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String whereToQuery(String where, String[] whereArgs) {
        if (where != null || !where.isEmpty() || whereArgs.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(" WHERE ");
        //...

        return builder.toString();
    }

    private String projectionToQuery(String[] projection) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String column : projection) {
            builder.append(column);
            if (i < projection.length - 1) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    public void createDatabase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database created with driver: " + meta.getDriverName());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        try {
            return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
            return -1;
        }
    }

    public long insertWithOnConflict(String table, String nullColumnHack,
                                     ContentValues initialValues, int conflictAlgorithm) throws SQLException {
        return 0;
    }
}
