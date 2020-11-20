package hcmus.student.map;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends FragmentActivity implements MainCallbacks {

    private ViewPager2 mViewPager;
    private TabLayout mTabs;
    private MapsFragment mMapFragment;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabs = findViewById(R.id.tabs);
        //Setup icon for each tab
        mTabs.getTabAt(0).setIcon(R.drawable.ic_tab_map);
        mTabs.getTabAt(1).setIcon(R.drawable.ic_tab_addressbook);

        mViewPager = findViewById(R.id.pager);
        mViewPager.setUserInputEnabled(false);
        mViewPager.setAdapter(new ViewPagerAdapter(this));
        final ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        mMapFragment = (MapsFragment) adapter.getFragment(0);
        mViewPager.setAdapter(adapter);
        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                mMapFragment = (MapsFragment) adapter.getFragment(0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //Should move to interface later
    public void backToPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    public void openMarkerInfo(Marker marker) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frameBottom, MarkerInfoFragment.newInstance(marker));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public void openAddContact(LatLng latLng) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frameBottom, AddContactFragment.newInstance(latLng));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void updateOnscreenMarker(LatLng coordinate, byte[] avt) {
        mMapFragment.createAvatarMarker(coordinate, avt);
    }
}