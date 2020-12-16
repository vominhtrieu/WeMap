package hcmus.student.map.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
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
import hcmus.student.map.utitlies.AddressProvider;

import static android.app.Activity.RESULT_OK;

public class AddContactFragment extends Fragment implements View.OnClickListener {
    MainActivity activity;
    Button btnAdd, btnCancel;
    EditText edtName;
    ImageButton btnCamera, btnFolder;
    ImageView ivAvatar;
    LatLng latLng;
    byte[] selectedImage;
    AddressProvider mAddressProvider;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    public static AddContactFragment newInstance(LatLng latLng) {
        AddContactFragment fragment = new AddContactFragment();
        Bundle args = new Bundle();
        args.putDouble("lat", latLng.latitude);
        args.putDouble("lng", latLng.longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        mAddressProvider = activity.getAddressProvider();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);

        btnAdd = view.findViewById(R.id.btnAddContact);
        btnCancel = view.findViewById(R.id.btnCancel);
        edtName = view.findViewById(R.id.edtName);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnFolder = view.findViewById(R.id.btnGallery);
        ivAvatar = view.findViewById(R.id.ivAvatar);

        ivAvatar.setEnabled(false);
        btnCamera.setOnClickListener(this);
        btnFolder.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        Bundle args = getArguments();

        latLng = new LatLng(args.getDouble("lat"), args.getDouble("lng"));
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCamera:
                cameraIntent();
                break;
            case R.id.btnGallery:
                galleryIntent();
                break;
            case R.id.btnAddContact:
                if (edtName.getText().toString().length() == 0) {
                    Toast.makeText(activity, "Place name is required!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        mAddressProvider.insertPlace(edtName.getText().toString(), new LatLng(latLng.latitude, latLng.longitude), selectedImage);
//                        Database db = new Database(getContext());
//                        db.insertPlace(edtName.getText().toString(), new LatLng(latLng.latitude, latLng.longitude), selectedImage);
                        activity.backToPreviousFragment();
//                        activity.updateOnscreenMarker(latLng, selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "This place is already in contact book", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnCancel:
                activity.backToPreviousFragment();
                break;
        }
    }

    private  void cameraIntent(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    private void galleryIntent(){
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
        float left = (float) (squareBitmapWidth - bitmap.getWidth()) / 2;
        float top = (float) (squareBitmapWidth - bitmap.getHeight()) / 2;
        canvas.drawBitmap(bitmap, left, top, paint);
        bitmap.recycle();
        return resultBitmap;
    }
}
