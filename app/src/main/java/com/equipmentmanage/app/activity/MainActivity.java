package com.equipmentmanage.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.ResultInfo;
import com.equipmentmanage.app.fragment.PutOnRecordFragment;
import com.equipmentmanage.app.fragment.SearchFragment;
import com.equipmentmanage.app.fragment.TaskFragment;
import com.equipmentmanage.app.fragment.UserFragment;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.GsonUtils1;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener {

    @BindView(R.id.viewPager2) //viewPager2
    public ViewPager2 viewPager2;

    @BindView(R.id.bottomNavigationView) //bottomNavigationView
    BottomNavigationView bottomNavigationView;

    private MenuItem menuItem;

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        bottomNavigationView.setOnItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.tab_two);;
    }

    @Override
    protected void initEvent() {

        bottomNavigationView.setOnItemSelectedListener(this);

        fragmentList.add(new TaskFragment());
        fragmentList.add(new PutOnRecordFragment());
        fragmentList.add(new SearchFragment());
        fragmentList.add(new UserFragment());

        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }
        });

        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Fragment fragment = fragmentList.get(position);
//                Bundle bundle=new Bundle();
//                bundle.putString(Constants.STATUS, config.getStatusList().get(position));
//                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        });
        viewPager2.setOffscreenPageLimit(1);



    }

    @Override
    protected void initData() {
//        getUserList();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        viewPager2.setCurrentItem(menuItem.getOrder());
        return false;
    }

    private void getUserList() {
        Subscribe.getUserList(new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                Log.i("result--->", result);
                //成功
                Toast.makeText(MainActivity.this, "请求成功！", Toast.LENGTH_SHORT).show();
                ResultInfo resultInfo = GsonUtils1.fromJson(result,
                        ResultInfo.class);
//                tvResult.setText(result);
            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(MainActivity.this, "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }, MainActivity.this));
    }

    private class ViewPageAdapter extends FragmentStateAdapter {
        public ViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
    }
}