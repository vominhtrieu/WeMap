package hcmus.student.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.Marker;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddContacts extends Fragment implements View.OnClickListener {

    Button btnAdd, btnCancel;
    EditText edtName;
    ImageButton ibtnCamera, ibtnFolder;
    ImageView imgAvata;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;

    public AddContacts() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_contacts, container, false);

        btnAdd = (Button) view.findViewById(R.id.btnAddContact);
        btnCancel = (Button) view.findViewById(R.id.btnCancelContact);
        edtName = (EditText) view.findViewById(R.id.edtName);
        ibtnCamera = (ImageButton) view.findViewById(R.id.ibtnCamera);
        ibtnFolder = (ImageButton) view.findViewById(R.id.ibtnFolder);
        imgAvata = (ImageView) view.findViewById(R.id.ivAvatar);

        ibtnCamera.setOnClickListener(this);
        ibtnFolder.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ibtnCamera:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.ibtnFolder:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, REQUEST_CODE_FOLDER);
                break;
            case R.id.btnAddContact:
                Activity activity = this.getActivity();
                if (edtName.getText().toString() == null) {
                    Toast.makeText(activity,"Place'name is required!", Toast.LENGTH_SHORT).show();
                }
                else {

                }
                break;
            case R.id.btnCancelContact:
                ((MapsActivity)getActivity()).backToFragmentBefore(R.id.frameMarkerInfo);

                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgAvata.setImageBitmap(bitmap);
        }

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgAvata.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
