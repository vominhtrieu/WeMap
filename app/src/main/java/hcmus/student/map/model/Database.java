package hcmus.student.map.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "Contact List";
    static final String TABLE_NAME = "Contact";
    static final String KEY_NAME = "NAME";
    static final String KEY_LATITUDE = "LATITUDE";
    static final String KEY_LONGITUDE = "LONGITUDE";
    static final String KEY_AVATAR = "AVATAR";
    static final String KEY_FAVORITE = "FAVORITE";
    static final int DATABASE_VERSION = 1;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private List<Place> cursorToPlaces(Cursor cursor) {
        ArrayList<Place> places = new ArrayList<>();
        if (cursor == null || !cursor.moveToFirst())
            return places;
        while (true) {
            Place place = new Place(cursor.getString(0),
                    new LatLng(cursor.getDouble(1), cursor.getDouble(2)),
                    cursor.getBlob(3), cursor.getString(4));

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
        return cursorToPlaces(cursor);
    }

    public List<Place> getAllPlaces() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, KEY_NAME);
        return cursorToPlaces(cursor);
    }

    public void deletePlace(Place place) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, KEY_LATITUDE + "=" + place.getLocation().latitude + " AND " + KEY_LONGITUDE + "=" + place.getLocation().longitude, null);
    }

    public void editPlace(Place place) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AVATAR, place.getAvatar());
        values.put(KEY_NAME, place.getName());
        db.update(TABLE_NAME, values, KEY_LATITUDE + "=" + place.getLocation().latitude + " AND " + KEY_LONGITUDE + "=" + place.getLocation().longitude, null);
        db.close();
    }

    public void insertPlace(String name, LatLng location, byte[] avatar) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindDouble(2, location.latitude);
        statement.bindDouble(3, location.longitude);
        if (avatar != null)
            statement.bindBlob(4, avatar);
        statement.bindString(5, "0");

        statement.executeInsert();
    }

    public void addFavorite(Double latitude, Double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_FAVORITE, "1");
        int numberChanged = db.update(TABLE_NAME, cv,
                KEY_LATITUDE + "=" + latitude + " AND " + KEY_LONGITUDE + "=" + longitude, null);
        Log.d("lat", "" + latitude);
        Log.d("lng", "" + longitude);
        Log.d("Num Add", "" + numberChanged);
    }

    public void removeFavorite(Double latitude, Double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_FAVORITE, "0");
        int a = db.update(TABLE_NAME, cv,
                KEY_LATITUDE + "=" + latitude + " AND " + KEY_LONGITUDE + "=" + longitude, null);
        Log.d("NumRemove", String.valueOf(a));
    }

    public ArrayList<Place> getPlacesNormal() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, KEY_FAVORITE + "='0'", null, null, null,
                KEY_NAME);
        ArrayList<Place> places = new ArrayList<>();
        if (cursor == null || !cursor.moveToFirst())
            return places;
        while (true) {
            //Edit later, lat <-> lng
            Place place = new Place(cursor.getString(0),
                    new LatLng(cursor.getDouble(1), cursor.getDouble(2)),
                    cursor.getBlob(3), cursor.getString(4));

            places.add(place);
            if (cursor.isLast()) {
                break;
            } else {
                cursor.moveToNext();
            }
        }
        return places;
    }

    public ArrayList<Place> getPlacesFavorite() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, KEY_FAVORITE + "='1'", null, null, null,
                KEY_NAME);
        ArrayList<Place> places = new ArrayList<>();
        if (cursor == null || !cursor.moveToFirst())
            return places;
        while (true) {
            //Edit later, lat <-> lng
            Place place = new Place(cursor.getString(0),
                    new LatLng(cursor.getDouble(1), cursor.getDouble(2)),
                    cursor.getBlob(3), cursor.getString(4));

            places.add(place);
            if (cursor.isLast()) {
                break;
            } else {
                cursor.moveToNext();
            }
        }
        return places;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s (%s TEXT, %s REAL, %s REAL, %s BLOB, %s TEXT, PRIMARY KEY (%s, %s))",
                TABLE_NAME, KEY_NAME, KEY_LATITUDE, KEY_LONGITUDE, KEY_AVATAR, KEY_FAVORITE, KEY_LATITUDE, KEY_LONGITUDE);

        SQLiteStatement statement = db.compileStatement(sql);
        statement.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
