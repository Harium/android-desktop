package android.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import java.sql.*;
import java.util.*;

public class SQLiteDatabase {

    String url;

    public SQLiteDatabase(String url) {
        this.url = url;
    }

    /**
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
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
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

        try {
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql);
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

    /**
     * Not Implemented
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

    /**
     * Not Implemented
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
}
