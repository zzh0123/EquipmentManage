package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseCompanyResultBean;
import com.equipmentmanage.app.bean.BaseCompanyTableBean;
import com.equipmentmanage.app.bean.ResultInfo;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.fragment.PutOnRecordFragment;
import com.equipmentmanage.app.fragment.PutOnRecordFragment1;
import com.equipmentmanage.app.fragment.SearchFragment;
import com.equipmentmanage.app.fragment.TaskFragment;
import com.equipmentmanage.app.fragment.UpDownLoadFragment;
import com.equipmentmanage.app.fragment.UserFragment;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.GsonUtils1;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener {

    public static void open(Context c){
        Intent i = new Intent(c, MainActivity.class);
        c.startActivity(i);
    }

    @BindView(R.id.viewPager2) //viewPager2
    public ViewPager2 viewPager2;

    @BindView(R.id.bottomNavigationView) //bottomNavigationView
    BottomNavigationView bottomNavigationView;

    private MenuItem menuItem;

    private List<Fragment> fragmentList = new ArrayList<>();

    private int pageNo = 1, pageSize = 2000;

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
        fragmentList.add(new PutOnRecordFragment1());
//        fragmentList.add(new UpDownLoadFragment());
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
        bottomNavigationView.setSelectedItemId(0);


    }

    @Override
    protected void initData() {
//        getUserList();

        getBaseCompanyList();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        L.i("zzz1--getItemId->" + item.getItemId());

        switch (item.getItemId()) {
            case R.id.task:
                viewPager2.setCurrentItem(0);
                break;
            case R.id.put_on_record:
                viewPager2.setCurrentItem(1);
                break;
//            case R.id.upload_download: // 上传下载
//                viewPager2.setCurrentItem(2);
//                break;
            case R.id.search:
                viewPager2.setCurrentItem(2);
                break;
            case R.id.user:
                viewPager2.setCurrentItem(3);
                break;
            default:
                break;
        }
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

    /**
     * 所属公司
     */
    private void getBaseCompanyList() {
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.pageNo, "" + pageNo); // pageNo
        params.put(Constant.pageSize, "" + pageSize); // pageSize
        Subscribe.getBaseCompanyList(params, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<BaseCompanyResultBean> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<BaseCompanyResultBean>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
                            BaseCompanyResultBean resultBean = baseBean.getResult();
                            if (resultBean != null) {
                                List<BaseCompanyResultBean.Records> dataList = resultBean.getRecords();
                                if (dataList != null && dataList.size() > 0) {
                                    BaseCompanyTableBean baseCompanyTableBean = new BaseCompanyTableBean();
                                    baseCompanyTableBean.id = "1";
                                    baseCompanyTableBean.content = GsonUtils.toJson(dataList);
                                    Long rowId = AppDatabase.getInstance(MainActivity.this).baseCompanyTableDao().insert(baseCompanyTableBean);
                                    if (rowId != null){
                                        EventBus.getDefault().post(ConstantValue.event_belong_company);
                                        Toasty.success(MainActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();
                                    } else {
                                        Toasty.error(MainActivity.this, R.string.download_fail, Toast.LENGTH_SHORT, true).show();
                                    }

                                } else {
                                    int row = AppDatabase.getInstance(MainActivity.this).baseAreaTableDao().deleteAll();
                                    L.i("zzz1-Company-row-->" + row);
                                    Toasty.error(MainActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                                }
                            } else {
                                Toasty.error(MainActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }
//
//                            adapter.notifyDataSetChanged();
                        } else {
                            Toasty.error(MainActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(MainActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(MainActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toasty.error(MainActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, MainActivity.this));
    }
}