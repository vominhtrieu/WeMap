package hcmus.student.map.address_book;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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
import android.location.Geocoder;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import hcmus.student.map.MainActivity;
import hcmus.student.map.R;
import hcmus.student.map.ViewPagerAdapter;
import hcmus.student.map.map.MapsFragment;
import hcmus.student.map.map.MapsFragmentCallbacks;
import hcmus.student.map.model.Database;
import hcmus.student.map.model.Place;
import hcmus.student.map.utitlies.AddressLine;
import hcmus.student.map.utitlies.OnAddressLineResponse;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static androidx.core.app.ActivityCompat.*;

public class AddressBookAdapter extends BaseAdapter {
    Database mDatabase;
    Context context;
    List<Place> places;
    final int REQUEST_CODE_CAMERA=123;
    final int REQUEST_CODE_FOLDER=456;
    Uri imageUri;
    ImageView ivAvatar;

    public AddressBookAdapter(Context context) {
        this.context = context;
        this.mDatabase = new Database(context);
        this.places = new ArrayList<>();
    }

    public void getUpdate() {
        places = mDatabase.getAllPlaces();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Place getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_place, null, false);
        }

        TextView txtListItemName = convertView.findViewById(R.id.txtListItemName);
        final TextView txtListItemAddressLine = convertView.findViewById(R.id.txtListItemAddressLine);
        txtListItemAddressLine.setText(R.string.txtLoadingAddressLine);
        final Place place = getItem(position);

        txtListItemName.setText(place.getName());
        LatLng location = place.getLocation();
        AddressLine addressLine = new AddressLine(new Geocoder(context), new OnAddressLineResponse() {
            @Override
            public void onAddressLineResponse(String addressLine) {
                if (addressLine != null) {
                    txtListItemAddressLine.setText(addressLine);
                } else {
                    txtListItemAddressLine.setText(R.string.txtNullLocation);
                }
            }
        });
        addressLine.execute(location);

        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));
        }
        final ImageButton btnBaselineMore=convertView.findViewById(R.id.btnMore);
        btnBaselineMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,btnBaselineMore);
                popupMenu.getMenuInflater().inflate(R.menu.menu_item,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.itemEdit:
                                GetDialogEdit(position);
                                break;
                            case R.id.itemDelete:
                                GetDialogDelete(position);
                                break;
                            case R.id.itemCancel:
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        ImageButton btnLocate=convertView.findViewById(R.id.btn_list_item_locate);
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).locatePlace(place.getLocation());
            }
        });
        return convertView;
    }

    private void GetDialogEdit(int position){
        final Dialog dialogEdit=new Dialog(context);
        dialogEdit.setContentView(R.layout.dialog_edit) ;
        final EditText edtNewName=dialogEdit.findViewById(R.id.edtNewName);

        Button btnOK=dialogEdit.findViewById(R.id.btnOK);
        Button btnCancel=dialogEdit.findViewById(R.id.btnCancel);
        ivAvatar=dialogEdit.findViewById(R.id.ivAvatar);
        final Place place = getItem(position);
        edtNewName.setText(place.getName());
        Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
        ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));

        ImageButton btnCamera=(ImageButton)dialogEdit.findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraIntent();
                Toast.makeText(context,"click image",Toast.LENGTH_SHORT).show();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place.setName(edtNewName.getText().toString());
                mDatabase.editPlace(place);
                notifyDataSetChanged();
                dialogEdit.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit.dismiss();
            }
        });
        dialogEdit.show();
    }
    private void GetDialogDelete(final int position){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure,You wanted to delete an address?");
        final Place place = getItem(position);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mDatabase.deletePlace(place);
                places.remove(position);
                //update Map.
                notifyDataSetChanged();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialogBuilder.show();
    }
    private  void CameraIntent(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        ((Activity)context).startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
    private void GalleryIntent(){
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType("image/*");
        ((Activity)context).startActivityForResult(intent1, REQUEST_CODE_FOLDER);
    }
    private void setSelectedImage(Bitmap bitmap) {

        ivAvatar.setImageBitmap(bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        this.selectedImage = stream.toByteArray();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
//                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                setSelectedImage(getCircularBitmap(bitmap));
//            }
//
//            if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
//                Uri uri = data.getData();
//                try {
//                    InputStream inputStream =  ((Activity)context).getContentResolver().openInputStream(uri);
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    setSelectedImage(getCircularBitmap(bitmap));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

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
