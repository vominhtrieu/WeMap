package hcmus.student.map.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "Contact List";
    static final String TABLE_NAME = "Contact";
    static final String KEY_ID = "ID";
    static final String KEY_NAME = "NAME";
    static final String KEY_LATITUDE = "LATITUDE";
    static final String KEY_LONGITUDE = "LONGITUDE";
    static final String KEY_AVATAR = "AVATAR";
    static final String KEY_FAVORITE = "FAVORITE";
    static final int DATABASE_VERSION = 1;

    private Context context;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    private List<Place> getPlacesFromCursor(Cursor cursor) {
        ArrayList<Place> places = new ArrayList<>();
        if (cursor == null || !cursor.moveToFirst())
            return places;
        while (true) {
            Place place = new Place(cursor.getInt(0), cursor.getString(1),
                    new LatLng(cursor.getDouble(2), cursor.getDouble(3)),
                    cursor.getBlob(4), cursor.getString(5));

            places.add(place);
            if (cursor.isLast()) {
                break;
            } else {
                cursor.moveToNext();
            }
        }
        return places;
    }

    public List<Place> searchForPlaces(String data) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, KEY_NAME + " LIKE '%" + data + "%'",
                null, null, null, KEY_NAME);
        return getPlacesFromCursor(cursor);
    }

    public List<Place> getAllPlaces() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null,
                KEY_FAVORITE + " DESC, " + KEY_NAME + " ASC");
        return getPlacesFromCursor(cursor);
    }

    public void deletePlace(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=" + id, null);
    }

    public void editPlace(Place place) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AVATAR, place.getAvatar());
        values.put(KEY_NAME, place.getName());
        db.update(TABLE_NAME, values, KEY_ID+"="+place.getId(), null);
        db.close();
    }

    public void insertPlace(String name, LatLng location, byte[] avatar) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(2, name);
        statement.bindDouble(3, location.latitude);
        statement.bindDouble(4, location.longitude);
        if (avatar != null)
            statement.bindBlob(5, avatar);
        statement.bindString(6, "0");
        statement.executeInsert();
    }

    public void addFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_FAVORITE, "1");
        db.update(TABLE_NAME, cv, KEY_ID + "=" + id, null);
    }

    public void removeFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_FAVORITE, "0");
        db.update(TABLE_NAME, cv, KEY_ID + "=" + id, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s REAL, %s REAL, %s BLOB, %s TEXT)",
                TABLE_NAME, KEY_ID, KEY_NAME, KEY_LATITUDE, KEY_LONGITUDE, KEY_AVATAR, KEY_FAVORITE);
        Log.e("sql", sql);
        SQLiteStatement statement = db.compileStatement(sql);
        statement.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
