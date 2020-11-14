package hcmus.student.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.maps.model.Marker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddContactFragment extends Fragment implements View.OnClickListener {
    MapsActivity activity;
    Button btnAdd, btnCancel;
    EditText edtName;
    ImageButton btnCamera, btnFolder;
    ImageView ivAvatar;
    Marker mMarker;
    byte[] selectedImage;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    public AddContactFragment(Marker marker) {
        this.mMarker = marker;
        this.selectedImage = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        activity = (MapsActivity) getActivity();

        btnAdd = (Button) view.findViewById(R.id.btnAddContact);
        btnCancel = (Button) view.findViewById(R.id.btnCancelContact);
        edtName = (EditText) view.findViewById(R.id.edtName);
        btnCamera = (ImageButton) view.findViewById(R.id.btnCamera);
        btnFolder = (ImageButton) view.findViewById(R.id.btnFolder);
        ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);

        btnCamera.setOnClickListener(this);
        btnFolder.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCamera:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.btnFolder:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, REQUEST_CODE_FOLDER);
                break;
            case R.id.btnAddContact:
                if (edtName.getText().toString().length() == 0) {
                    Toast.makeText(activity, "Place name is required!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Database db = new Database(getContext());
                        db.InsertPlace(edtName.getText().toString(), mMarker.getPosition().latitude, mMarker.getPosition().longitude, selectedImage);

                    }
                    catch (Exception e) {
                        Toast.makeText(activity, "This place is already in contact book", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnCancelContact:
                activity.backToPreviousFragment(R.id.frameMarkerInfo);
                break;
        }
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
            setSelectedImage(bitmap);
        }

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = activity.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                setSelectedImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
