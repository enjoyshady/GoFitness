package com.gofitness.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.gofitness.R;
import com.gofitness.database.Constants;
import com.gofitness.fragment.MainActivity.ExerciseFragment;
import com.gofitness.fragment.MainActivity.MeFragment;
import com.gofitness.fragment.MainActivity.TodayFragment;
import com.gofitness.utils.SharedPreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private Fragment[] mFragments;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize view
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setUserInputEnabled(false);
        mBottomNavigationView = findViewById(R.id.nav_view);
        // initialize Fragment
        mFragments = new Fragment[]{
                ExerciseFragment.newInstance(),
                TodayFragment.newInstance(),
                MeFragment.newInstance()
        };
        // initialize pager
        mViewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return mFragments[position];
            }

            @Override
            public int getItemCount() {
                return mFragments.length;
            }
        });
        // 监听换页
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBottomNavigationView.setSelectedItemId(R.id.nav_exercise);
                        break;
                    case 1:
                        mBottomNavigationView.setSelectedItemId(R.id.nav_today);
                        break;
                    case 2:
                        mBottomNavigationView.setSelectedItemId(R.id.nav_me);
                        break;
                }
            }
        });
        // 监听nav点击
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_exercise:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_today:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_me:
                    mViewPager.setCurrentItem(2);
                    return true;
                default:
                    return false;
            }
        });
        if (!SharedPreferenceUtils.getBoolean(this, Constants.IS_INITIALIZATION_USER_INFORMATION, false)) {
            // 是第一次打开 , 在里面点击确认，就不会进去了
            InitializationActivity.startActivity(this);
        }
    }
}