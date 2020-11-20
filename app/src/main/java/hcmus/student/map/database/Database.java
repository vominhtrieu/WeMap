package hcmus.student.map.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "Contact List";
    static final String TABLE_NAME = "Contact";
    static final String KEY_NAME = "NAME";
    static final String KEY_LATITUDE = "LATITUDE";
    static final String KEY_LONGITUDE = "LONGITUDE";
    static final String KEY_AVATAR = "AVATAR";
    static final int DATABASE_VERSION = 1;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList<Place> getAllPlaces() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, KEY_NAME);
        ArrayList<Place> places = new ArrayList<>();
        if (cursor == null || !cursor.moveToFirst())
            return places;
        while (true) {
            Place place = new Place(cursor.getString(0), cursor.getDouble(1),
                    cursor.getDouble(2), cursor.getBlob(3));

            places.add(place);
            if (cursor.isLast()) {
                break;
            } else {
                cursor.moveToNext();
            }
        }
        return places;
    }

    public void insertPlace(String name, Double longitude, Double latitude, byte[] avatar) {
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
        String sql = String.format("CREATE TABLE %s (%s TEXT, %s REAL, %s REAL, %s BLOB, PRIMARY KEY (%s, %s))",
                TABLE_NAME, KEY_NAME, KEY_LATITUDE, KEY_LONGITUDE, KEY_AVATAR, KEY_LATITUDE, KEY_LONGITUDE);

        SQLiteStatement statement = db.compileStatement(sql);
        statement.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
