package hcmus.student.map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "Contact List";
    static final String TABLE_NAME = "Contact";
    static final int DATABASE_VERSION = 1;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void QueryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public Cursor GetData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void InsertPlace(String name, Double longitude, Double latitude, byte[] avatar) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindDouble(2, latitude);
        statement.bindDouble(3, longitude);
        if (avatar != null)
            statement.bindBlob(4, avatar);

        statement.executeInsert();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME +
                        "(NAME TEXT," +
                        "LATITUDE REAL," +
                        "LONGITUDE REAL," +
                        "AVATAR BLOB," +
                        "PRIMARY KEY (LATITUDE, LONGITUDE))";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
