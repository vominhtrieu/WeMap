package hcmus.student.map.address_book;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.model.Database;
import hcmus.student.map.model.Place;

import static android.app.Activity.RESULT_OK;

public class EditPlaceFragment extends Fragment implements View.OnClickListener {
    MainActivity activity;
    Button btnOK, btnCancel;
    EditText edtNewName;
    ImageButton btnCamera, btnFolder;
    ImageView ivAvatar;
    Place place;
    LatLng latLng;
    byte[] selectedImage;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    public static EditPlaceFragment newInstance(Place place) {
        EditPlaceFragment fragment = new EditPlaceFragment();
        Bundle args = new Bundle();

        args.putInt("id", place.getId());
        args.putString("name", place.getName());
        args.putDouble("lat", place.getLocation().latitude);
        args.putDouble("lng", place.getLocation().latitude);
        args.putByteArray("avatar", place.getAvatar());
        args.putString("favorite", place.getFavorite());

        Log.d("name", place.getName());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit, container, false);

        Bundle args = getArguments();


        btnOK = view.findViewById(R.id.btnOK);
        btnCancel = view.findViewById(R.id.btnCancel);
        edtNewName = view.findViewById(R.id.edtNewName);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnFolder = view.findViewById(R.id.btnGallery);
        ivAvatar = view.findViewById(R.id.ivAvatar);

        latLng = new LatLng(args.getDouble("lat"), args.getDouble("lng"));

        place = new Place(args.getInt("id"), args.getString("name"), latLng, args.getByteArray("avatar"), args.getString("favorite"));

        edtNewName.setText(place.getName());
        Log.d("name", place.getName());
        Log.d("id", "" + place.getId());

        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ivAvatar.setBackground(new BitmapDrawable(view.getResources(), bmp));
        }

        btnCamera.setOnClickListener(this);
        btnFolder.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCamera:
                CameraIntent();
                break;
            case R.id.btnGallery:
                GalleryIntent();
                break;
            case R.id.btnOK:
                if (edtNewName.getText().toString().length() == 0) {
                    Toast.makeText(activity, "Place name is required!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Database db = new Database(getContext());
                        place.setName(edtNewName.getText().toString());
                        if (selectedImage != null) {
                            place.setAvatar(selectedImage);
                        }
                        db.editPlace(place);
                        activity.backToPreviousFragment();
                        activity.updateOnscreenMarker(latLng, selectedImage);
                    } catch (Exception e) {
                        activity.updateOnscreenMarker(latLng, selectedImage);
                        activity.backToPreviousFragment();
                    }
                }
                break;
            case R.id.btnCancel:
                activity.backToPreviousFragment();
                break;
        }
    }

    private void CameraIntent() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    private void GalleryIntent() {
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType("image/*");
        startActivityForResult(intent1, REQUEST_CODE_FOLDER);
    }

    private void setSelectedImage(Bitmap bitmap) {
        ivAvatar.setImageBitmap(bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.selectedImage = stream.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            setSelectedImage(getCircularBitmap(bitmap));
        }

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = activity.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                setSelectedImage(getCircularBitmap(bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int squareBitmapWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap resultBitmap = Bitmap.createBitmap(squareBitmapWidth, squareBitmapWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        float left = (squareBitmapWidth - bitmap.getWidth()) / 2;
        float top = (squareBitmapWidth - bitmap.getHeight()) / 2;
        canvas.drawBitmap(bitmap, left, top, paint);
        bitmap.recycle();
        return resultBitmap;
    }
}
