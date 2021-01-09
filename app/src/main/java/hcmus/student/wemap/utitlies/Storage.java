package hcmus.student.wemap.utitlies;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Storage {
    private Context context;

    public Storage(Context context) {
        this.context = context;
    }

    public String saveToInternalStorage(Bitmap bitmapImage) {
        if (bitmapImage == null) {
            return null;
        }
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        Random random = new Random();
        File path = new File(directory,
                System.currentTimeMillis() + Long.toString(random.nextLong()));

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path.getAbsolutePath();
    }

    public Bitmap readImageFromInternalStorage(String filename) {
        if (filename == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            File f = new File(filename);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {

        }
        return bitmap;
    }
}
