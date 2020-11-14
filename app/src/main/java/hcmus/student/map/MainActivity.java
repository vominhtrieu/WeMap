package hcmus.student.map;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends FragmentActivity {

    private ViewPager2 mViewPager;
    private TabLayout mTabs;

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

    }
}