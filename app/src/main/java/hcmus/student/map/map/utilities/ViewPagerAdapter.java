package hcmus.student.map.map.utilities;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import hcmus.student.map.address_book.AddressBookFragment;
import hcmus.student.map.direction.DirectionFragment;
import hcmus.student.map.map.MapsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> fragmentList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragmentList = new ArrayList<>();
        fragmentList.add(MapsFragment.newInstance());
        fragmentList.add(DirectionFragment.newInstance(null, null));
        fragmentList.add(AddressBookFragment.newInstance());
    }

    public ArrayList<Fragment> getFragmentList() {
        return fragmentList;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position >= 0 && position <= 2) {
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public Fragment getFragment(int position) {
        return fragmentList.get(position);
    }
}

